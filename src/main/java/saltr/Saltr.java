/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import saltr.parser.response.AppData;
import saltr.parser.response.SaltrResponse;
import saltr.parser.response.level.LevelData;
import saltr.repository.IRepository;

import java.text.MessageFormat;
import java.util.*;

public class Saltr {
    private static Saltr saltr;
    //    private static final String SALTR_API_URL = "http://saltapi.includiv.com/httpjson.action";
    private static final String SALTR_API_URL = "http://localhost:8081/httpjson.action";
    //    private static final String SALTR_URL = "http://saltadmin.includiv.com/httpjson.action";
    private static final String SALTR_URL = "http://localhost:8085/httpjson.action";
    private static final String COMMAND_APP_DATA = "APPDATA";
    private static final String COMMAND_ADD_PROPERTY = "ADDPROP";
    private static final String COMMAND_SAVE_OR_UPDATE_FEATURE = "SOUFTR";

    //
    private static final String APP_DATA_URL_CACHE = "app_data_cache.json";
    private static final String APP_DATA_URL_INTERNAL = "saltr/app_data.json";
    private static final String LEVEL_DATA_URL_LOCAL_TEMPLATE = "saltr/pack_{0}/level_{1}.json";
    private static final String LEVEL_DATA_URL_CACHE_TEMPLATE = "pack_{0}_level_{1}.json";

    public static final String PROPERTY_OPERATIONS_INCREMENT = "inc";
    public static final String PROPERTY_OPERATIONS_SET = "set";

    private static final String RESULT_SUCCEED = "SUCCEED";
    private static final String RESULT_ERROR = "FAILURE";

    private IRepository repository;
    private String saltUserId;
    private Boolean isLoading;
    private Boolean ready;
    private Partner partner;

    private Deserializer deserializer;
    private String instanceKey;
    private Map<String, Feature> features;
    private List<LevelPackStructure> levelPackStructures;
    private List<Experiment> experiments;
    private Device device;
    private String appVersion;
    private Boolean isInDevMode;
    private SaltrHttpDataHandler saltrHttpDataHandler;

    private Saltr(String instanceKey) {
        features = new HashMap<String, Feature>();
        this.instanceKey = instanceKey;
        deserializer = new Deserializer();
        isLoading = false;
        ready = false;
        isInDevMode = true;
    }

    public static Saltr getSaltr(String instanceKey) {
        if (saltr == null) {
            return new Saltr(instanceKey);
        }
        return saltr;
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

    public void getAppData(SaltrHttpDataHandler saltrHttpDataHandler) {
        this.saltrHttpDataHandler = saltrHttpDataHandler;
        loadAppData();
    }

    private void loadAppDataSuccessHandler(AppData appData) {
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
        saltrHttpDataHandler.onSuccess();

        if (isInDevMode) {
            syncFeatures();
        }
    }

    private void loadAppDataFailHandler(String msg) {
        saltrHttpDataHandler.onFail();
        this.isLoading = false;
        this.ready = false;
        System.out.println(msg);
    }

    private void loadAppDataInternal() {
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

    public void getLevelDataBody(LevelPackStructure levelPackData, LevelStructure levelData, Boolean useCache, SaltrHttpDataHandler saltrHttpDataHandler) {
        this.saltrHttpDataHandler = saltrHttpDataHandler;
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

    private void levelLoadSuccessHandler(LevelStructure levelData, LevelData data) {
        levelData.parseData(data);
        saltrHttpDataHandler.onSuccess();

    }

    private void levelLoadErrorHandler() {
        System.out.println("[SaltClient] ERROR: Level data is not loaded.");
        saltrHttpDataHandler.onFail();
    }

    private void loadLevelDataLocally(LevelPackStructure levelPackData, LevelStructure levelData, String cachedFileName) {
        if (loadLevelDataCached(levelData, cachedFileName) == true) {
            return;
        }
        loadLevelDataInternal(levelPackData, levelData);
    }

    private boolean loadLevelDataCached(LevelStructure levelData, String cachedFileName) {
        System.out.println("[SaltClient::loadLevelData] LOADING LEVEL DATA CACHE IMMEDIATELY.");
        LevelData data = (LevelData) repository.getObjectFromCache(cachedFileName);
        if (data != null) {
            levelLoadSuccessHandler(levelData, data);
            return true;
        }
        return false;
    }

    private void loadLevelDataFromServer(LevelPackStructure levelPackData, LevelStructure levelData, Boolean forceNoCache) {
        String dataUrl = forceNoCache ? levelData.getDataUrl() + "?_time_=" + new Date().getTime() : levelData.getDataUrl();
        String cachedFileName = getCachedFileName((new Integer(levelPackData.getIndex())).toString(),
                (new Integer(levelData.getIndex())).toString());

        CallBackDetails details = new CallBackDetails(DataType.LEVEL);
        details.setCachedFileName(cachedFileName);
        details.setLevelData(levelData);
        details.setLevelPackData(levelPackData);

        try {
            SaltrHttpConnection connection = new SaltrHttpConnection(dataUrl);
            connection.call(this, details);
        } catch (Exception e) {
            levelDataAssetLoadErrorHandler(details);
        }
    }

    void levelDataAssetLoadedHandler(String data, CallBackDetails details) {
        if (data == null) {
            loadLevelDataLocally(details.getLevelPackData(), details.getLevelData(), details.getCachedFileName());
        }
        else {
            Gson gson = new Gson();
            levelLoadSuccessHandler(details.getLevelData(), gson.fromJson(data, LevelData.class));
            repository.cacheObject(details.getCachedFileName(), details.getLevelData().getVersion(), data);
        }
    }

    void levelDataAssetLoadErrorHandler(CallBackDetails details) {
        loadLevelDataLocally(details.getLevelPackData(), details.getLevelData(), details.getCachedFileName());
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

        SaltrHttpConnection connection = createAppDataConnection();
        CallBackDetails details = new CallBackDetails(DataType.APP);

        try {
            connection.call(this, details);
        } catch (Exception e) {
            appDataAssetLoadErrorHandler();
        }
    }

    void appDataAssetLoadErrorHandler() {
        System.out.println("[SaltAPI] App data is failed to load.");
        loadAppDataInternal();
    }

    void appDataAssetLoadCompleteHandler(String data) {
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

    private SaltrHttpConnection createAppDataConnection() {
        Gson gson = new Gson();
        Map<String, Object> args = new HashMap<String, Object>();
        if (device != null) {
            args.put("device", device);
        }
        if (partner != null) {
            args.put("partner", partner);
        }
        args.put("instanceKey", instanceKey);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("arguments", gson.toJson(args)));
        params.add(new BasicNameValuePair("command", Saltr.COMMAND_APP_DATA));

        return new SaltrHttpConnection(Saltr.SALTR_API_URL, params);
    }

    public void syncFeatures() {
        Gson gson = new Gson();

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

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("command", Saltr.COMMAND_SAVE_OR_UPDATE_FEATURE));
        params.add(new BasicNameValuePair("instanceKey", instanceKey));
        params.add(new BasicNameValuePair("data", gson.toJson(featureList)));
        if (appVersion != null) {
            params.add(new BasicNameValuePair("appVersion", appVersion));
        }

        SaltrHttpConnection connection = new SaltrHttpConnection(SALTR_URL, params);
        CallBackDetails details = new CallBackDetails(DataType.FEATURE);
        try {
            connection.call(this, details);
        } catch (Exception e) {
        }
    }

    private String getCachedFileName(String packId, String levelId) {
        List<String> params = new ArrayList<String>();
        params.add(packId);
        params.add(levelId);
        return String.format(LEVEL_DATA_URL_CACHE_TEMPLATE, params);
    }
}
