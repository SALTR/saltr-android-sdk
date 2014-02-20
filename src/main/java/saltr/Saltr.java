/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import saltr.parser.response.AppData;
import saltr.parser.response.SaltrResponse;
import saltr.parser.response.level.LevelData;
import saltr.repository.IRepository;
import saltr.resource.HttpConnection;

import java.text.MessageFormat;
import java.util.*;

public class Saltr {
    //    protected static final String SALTR_API_URL = "http://saltapi.includiv.com/httpjson.action";
    protected static final String SALTR_API_URL = "http://localhost:8081/httpjson.action";
    //    protected static final String SALTR_URL = "http://saltadmin.includiv.com/httpjson.action";
    protected static final String SALTR_URL = "http://localhost:8085/httpjson.action";
    protected static final String COMMAND_APP_DATA = "APPDATA";
    protected static final String COMMAND_ADD_PROPERTY = "ADDPROP";
    protected static final String COMMAND_SAVE_OR_UPDATE_FEATURE = "SOUFTR";

    //
    protected static final String APP_DATA_URL_CACHE = "app_data_cache.json";
    protected static final String APP_DATA_URL_INTERNAL = "saltr/app_data.json";
    protected static final String LEVEL_DATA_URL_LOCAL_TEMPLATE = "saltr/pack_{0}/level_{1}.json";
    protected static final String LEVEL_DATA_URL_CACHE_TEMPLATE = "pack_{0}_level_{1}.json";

    public static final String PROPERTY_OPERATIONS_INCREMENT = "inc";
    public static final String PROPERTY_OPERATIONS_SET = "set";

    protected static final String RESULT_SUCCEED = "SUCCEED";
    protected static final String RESULT_ERROR = "FAILURE";

    protected IRepository repository;
    protected String saltUserId;
    protected Boolean isLoading;
    protected Boolean ready;
    protected Partner partner;

    protected Deserializer deserializer;
    protected String instanceKey;
    protected Map<String, Feature> features;
    protected List<LevelPackStructure> levelPackStructures;
    protected List<Experiment> experiments;
    protected Device device;
    private String appVersion;
    private Boolean isInDevMode;
    private AppDataHandler appDataHandler;
    private LevelDataHandler levelDataHandler;

    public Saltr(String instanceKey) {
        features = new HashMap<String, Feature>();
        this.instanceKey = instanceKey;
        deserializer = new Deserializer();
        isLoading = false;
        ready = false;
        isInDevMode = true;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    public Boolean getReady() {
        return ready;
    }

    public Map<String, Feature> getFeatures() {
        return features;
    }

    public List<LevelPackStructure> getLevelPackStructures() {
        return levelPackStructures;
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public Feature getFeature(String token) {
        return features.get(token);
    }

    public void initPartner(String partnerId, String partnerType) {
        this.partner = new Partner(partnerId, partnerType);
    }

    public void initDevice(String deviceId, String deviceType) {
        this.device = new Device(deviceId, deviceType);
    }

    /**
     * If you want to have a feature synced with SALTR you should call define before getAppData call.
     */
    public void defineFeature(String token, Map<String, String> properties) {
        Feature feature = features.get(token);
        if (feature == null) {
            features.put(token, new Feature(token, null, properties));
        }
        else {
            feature.setDefaultProperties(properties);
        }
    }

    public void getAppData(AppDataHandler appDataHandler) {
        this.appDataHandler = appDataHandler;
        loadAppData();
    }

    protected void loadAppDataSuccessHandler(AppData appData) {
        isLoading = false;
        ready = true;
        saltUserId = appData.getSaltId().toString();

        experiments = deserializer.decodeExperimentInfo(appData);
        levelPackStructures = deserializer.decodeLevels(appData);
        Map<String, Feature> saltrFeatures = deserializer.decodeFeatures(appData);
        //merging with defaults...
        for (Map.Entry<String, Feature> entry : saltrFeatures.entrySet()) {
            Feature saltrFeature = entry.getValue();
            Feature defaultFeature = features.get(entry.getKey());
            if (defaultFeature != null) {
                saltrFeature.setDefaultProperties(defaultFeature.getDefaultProperties());
            }
            features.put(entry.getKey(), saltrFeature);
        }

        System.out.println("[SaltClient] packs=" + levelPackStructures.size());
        appDataHandler.onGetAppDataSuccess();

        if (isInDevMode) {
            syncFeatures();
        }
    }

    private void loadAppDataFailHandler(String msg) {
        appDataHandler.onGetAppDataFail();
        this.isLoading = false;
        this.ready = false;
        System.out.println(msg);
    }

    protected void loadAppDataInternal() {
        System.out.println("[SaltClient] NO Internet available - loading internal app data.");
        if (repository == null) {
            loadAppDataFailHandler("[SaltClient] ERROR: Repository is not initialized.");
        }

        Object data = repository.getObjectFromCache(APP_DATA_URL_CACHE);

        if (data != null) {
            System.out.println("[SaltClient] Loading App data from Cache folder.");
            loadAppDataSuccessHandler((AppData) data);
        }
        else {
            System.out.println("[SaltClient] Loading App data from application folder.");
            data = repository.getObjectFromApplication(APP_DATA_URL_INTERNAL);
            if (data != null) {
                loadAppDataSuccessHandler((AppData) data);
            }
            else {
                loadAppDataFailHandler("[SaltClient] ERROR: Level Packs are not loaded.");
            }
        }
    }

    public void getLevelDataBody(LevelPackStructure levelPackData, LevelStructure levelData, Boolean useCache, LevelDataHandler levelDataHandler) {
        this.levelDataHandler = levelDataHandler;
        if (!useCache) {
            loadLevelDataFromServer(levelPackData, levelData, true);
            return;
        }

        //if there are no version change than load from cache
        String[] values = {
                String.valueOf(levelPackData.getIndex()), String.valueOf(levelData.getIndex())
        };
        String cachedFileName = MessageFormat.format(LEVEL_DATA_URL_CACHE_TEMPLATE, values);
        if (levelData.getVersion().equals(repository.getObjectVersion(cachedFileName))) {
            loadLevelDataCached(levelData, cachedFileName);
        }
        else {
            loadLevelDataFromServer(levelPackData, levelData, false);
        }

    }

    protected void levelLoadSuccessHandler(LevelStructure levelData, LevelData data) {
        levelData.parseData(data);
        levelDataHandler.onGetLevelDataBodySuccess();

    }

    protected void levelLoadErrorHandler() {
        System.out.println("[SaltClient] ERROR: Level data is not loaded.");
        levelDataHandler.onGetLevelDataBodyFail();
    }

    protected void loadLevelDataLocally(LevelPackStructure levelPackData, LevelStructure levelData, String cachedFileName) {
        if (loadLevelDataCached(levelData, cachedFileName) == true) {
            return;
        }
        loadLevelDataInternal(levelPackData, levelData);
    }

    protected boolean loadLevelDataCached(LevelStructure levelData, String cachedFileName) {
        System.out.println("[SaltClient::loadLevelData] LOADING LEVEL DATA CACHE IMMEDIATELY.");
        LevelData data = (LevelData) repository.getObjectFromCache(cachedFileName);
        if (data != null) {
            levelLoadSuccessHandler(levelData, data);
            return true;
        }
        return false;
    }

    protected void loadLevelDataFromServer(LevelPackStructure levelPackData, LevelStructure levelData, Boolean forceNoCache) {
        String dataUrl = forceNoCache ? levelData.getDataUrl() + "?_time_=" + new Date().getTime() : levelData.getDataUrl();
        List<String> params = new ArrayList<String>();
        params.add((new Integer(levelPackData.getIndex())).toString());
        params.add((new Integer(levelData.getIndex())).toString());
        String cachedFileName = String.format(LEVEL_DATA_URL_CACHE_TEMPLATE, params);
        try {
            HttpConnection connection = new HttpConnection(dataUrl);
            String data = connection.excutePost();

            if (data == null) {
                loadLevelDataLocally(levelPackData, levelData, cachedFileName);
            }
            else {
                Gson gson = new Gson();
                levelLoadSuccessHandler(levelData, gson.fromJson(data, LevelData.class));
                repository.cacheObject(cachedFileName, levelData.getVersion(), data);
            }
        } catch (Exception e) {
            loadLevelDataLocally(levelPackData, levelData, cachedFileName);
        }
    }


    private void loadLevelDataInternal(LevelPackStructure levelPackData, LevelStructure levelData) {

        String[] values = {
                String.valueOf(levelPackData.getIndex()), String.valueOf(levelData.getIndex())
        };
        String url = MessageFormat.format(LEVEL_DATA_URL_LOCAL_TEMPLATE, values);

        LevelData data = (LevelData) repository.getObjectFromApplication(url);
        if (data != null) {
            levelLoadSuccessHandler(levelData, data);
        }
        else {
            levelLoadErrorHandler();
        }
    }

    private void loadAppData() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        ready = false;
        HttpConnection connection = createAppDataConnection();

        String response = connection.excutePost();
        if (response != null) {
            appDataAssetLoadCompleteHandler(response);
        }
        else {
            appDataAssetLoadErrorHandler();
        }
    }

    private void appDataAssetLoadErrorHandler() {
        System.out.println("[SaltAPI] App data is failed to load.");
        loadAppDataInternal();
    }

    private void appDataAssetLoadCompleteHandler(String data) {
        System.out.println("[SaltAPI] App data is loaded.");
        Gson gson = new Gson();
        SaltrResponse<AppData> response = gson.fromJson(data, new TypeToken<SaltrResponse<AppData>>() {
        }.getType());

        if (!response.getStatus().equals(Saltr.RESULT_SUCCEED)) {
            loadAppDataInternal();
        }
        else {
            System.out.println("[SaltClient] Loaded App data. json=" + data);
            loadAppDataSuccessHandler(response.getResponseData());
            repository.cacheObject(APP_DATA_URL_CACHE, "0", response.getResponseData());
        }
    }

    private HttpConnection createAppDataConnection() {
        Gson gson = new Gson();
        Map<String, Object> args = new HashMap<String, Object>();
        if (device != null) {
            args.put("device", device);
        }
        if (partner != null) {
            args.put("partner", partner);
        }
        args.put("instanceKey", instanceKey);

        Map<String, String> params = new HashMap<String, String>();
        params.put("arguments", gson.toJson(args));
        params.put("command", Saltr.COMMAND_APP_DATA);

        return new HttpConnection(Saltr.SALTR_API_URL, params);
    }

    public void syncFeatures() {
        Gson gson = new Gson();
        Map<String, String> urlVars = new HashMap<String, String>();
        urlVars.put("command", Saltr.COMMAND_SAVE_OR_UPDATE_FEATURE);
        urlVars.put("instanceKey", instanceKey);

        if (appVersion != null) {
            urlVars.put("appVersion", appVersion);
        }

        List<Map<String, String>> featureList = new ArrayList<Map<String, String>>();
        Map<String, String> tempMap;
        Feature feature;
        for (Map.Entry<String, Feature> entry : features.entrySet()) {
            feature = entry.getValue();
            if (feature.getDefaultProperties() != null) {
                tempMap = new HashMap<String, String>();
                tempMap.put("token", feature.getToken());
                tempMap.put("value", gson.toJson(feature.getDefaultProperties()));
                featureList.add(tempMap);
            }
        }
        urlVars.put("data", gson.toJson(featureList));

        HttpConnection connection = new HttpConnection(SALTR_URL, urlVars);
        SaltrResponse response = gson.fromJson(connection.excutePost(), SaltrResponse.class);
    }
}
