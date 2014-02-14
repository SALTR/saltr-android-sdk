/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import com.google.gson.Gson;
import saltr.parser.response.AppData;
import saltr.parser.response.SaltrResponse;
import saltr.parser.response.level.LevelData;
import saltr.repository.IRepository;
import saltr.resource.HttpConnection;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;

public class Saltr implements ObservableSaltr {
//    protected static final String SALTR_API_URL = "http://saltapi.includiv.com/httpjson.action";
    protected static final String SALTR_API_URL = "http://localhost.:8081/httpjson.action";
    protected static final String SALTR_URL = "http://saltadmin.includiv.com/httpjson.action";
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
    protected static final String RESULT_ERROR = "ERROR";

    protected IRepository repository;
    protected String saltUserId;
    protected Boolean isLoading;
    protected Boolean ready;
    protected Partner partner;

    protected Deserializer deserializer;
    protected String instanceKey;
    protected List<Feature> features;
    protected List<LevelPackStructure> levelPackStructures;
    protected List<Experiment> experiments;
    protected Device device;

    private List<SaltrObserver> observers;

    public Saltr(String instanceKey) {
        this.instanceKey = instanceKey;
        this.deserializer = new Deserializer();
        this.isLoading = false;
        this.ready = false;
        this.observers = new ArrayList<SaltrObserver>();
    }

    public Boolean getReady() {
        return ready;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public List<LevelPackStructure> getLevelPackStructures() {
        return levelPackStructures;
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public Feature getFeatureByToken(String token) {
        for (Feature feature: features) {
            if (feature.getToken().equals(token)) {
                return feature;
            }
        }
        return null;
    }

    public void initPartner(String partnerId, String partnerType) {
        this.partner = new Partner(partnerId, partnerType);
    }

    public void initDevice(String deviceId, String deviceType) {
        this.device = new Device(deviceId, deviceType);
    }

    public void getAppData() {
        loadAppData();
    }

    protected void loadAppDataSuccessHandler(AppData appData) {
        this.isLoading = false;
        this.ready = true;
        this.saltUserId = appData.getSaltId().toString();
        this.deserializer.decode(appData);
        this.features = this.deserializer.getFeatures();
        this.levelPackStructures = this.deserializer.getLevelPackStructures();
        this.experiments = this.deserializer.getExperiments();
        System.out.println("[SaltClient] packs=" + this.deserializer.getLevelPackStructures().size());
    }

    private void loadAppDataFailHandler() {
        this.isLoading = false;
        this.ready = false;
        System.out.println("[SaltClient] ERROR: Level Packs are not loaded.");
    }

    protected void loadAppDataInternal() {
        System.out.println("[SaltClient] NO Internet available - so loading internal app data.");
        AppData data = (AppData) repository.getObjectFromCache(APP_DATA_URL_CACHE);
        if (data != null) {
            System.out.println("[SaltClient] Loading App data from Cache folder.");
            loadAppDataSuccessHandler(data);
        }
        else {
            System.out.println("[SaltClient] Loading App data from application folder.");
            data = (AppData) repository.getObjectFromApplication(APP_DATA_URL_INTERNAL);
            if (data != null) {
                loadAppDataSuccessHandler(data);
            }
            else {
                loadAppDataFailHandler();
            }
        }
    }

    public void getLevelDataBody(LevelPackStructure levelPackData, LevelStructure levelData, Boolean useCache) {
        if (useCache == null) {
            useCache = true;
        }

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
        } else {
            loadLevelDataFromServer(levelPackData, levelData, false);
        }

    }

    protected void levelLoadSuccessHandler(LevelStructure levelData, LevelData data) {
        levelData.parseData(data);
    }

    protected void levelLoadErrorHandler() {
        System.out.println("[SaltClient] ERROR: Level data is not loaded.");
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
            HttpConnection connection = new HttpConnection(dataUrl, null);
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
        Gson gson = new Gson();
        SaltrResponse response = gson.fromJson(connection.excutePost(), SaltrResponse.class);
    }

    private void appDataAssetLoadErrorHandler() {
        System.out.println("[SaltAPI] App data is failed to load.");
        loadAppDataInternal();
    }

    private void appDataAssetLoadCompleteHandler(String data) {
        System.out.println("[SaltAPI] App data is loaded.");
        Gson gson = new Gson();
        SaltrResponse<AppData> response = gson.fromJson(data, SaltrResponse.class);
        System.out.println("[SaltClient] Loaded App data. json=" + data);
        if (response.getResponseData() == null || response.getStatus() != Saltr.RESULT_SUCCEED) {
            loadAppDataInternal();
        }
        else {
            loadAppDataSuccessHandler(response.getResponseData());
            repository.cacheObject(APP_DATA_URL_CACHE, "0", response.getResponseData());
        }
    }

    private HttpConnection createAppDataConnection() {
        try {
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

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveOrUpdateFeature(List<Feature> featureList) {
        Map<String, String> urlVars = new HashMap<String, String>();
        urlVars.put("command", Saltr.COMMAND_SAVE_OR_UPDATE_FEATURE);
        urlVars.put("instanceKey", instanceKey);

        List<String> list = new ArrayList<String>();
        Feature feature;
        int len = featureList.size();
        Map<String, String> tempMap;
        for (int i = 0; i < len; ++i) {
            tempMap = new HashMap<String, String>();
            feature = featureList.get(i);
            tempMap.put("token", feature.getToken());
            tempMap.put("value", feature.getDefaultProperties().toString());
            list.add(tempMap.toString());
        }
        urlVars.put("data", list.toString());
//        var ticket:ResourceURLTicket = new ResourceURLTicket(Saltr.SALTR_URL, urlVars);
//        var resource:Resource = new Resource("saveOrUpdateFeature", ticket, saveOrUpdateFeatureLoadCompleteHandler, saveOrUpdateFeatureLoadErrorHandler);
//        resource.load();
    }

    private void saveOrUpdateFeatureLoadCompleteHandler() {
        System.out.println("[Saltr] Feature saved or updated.");

    }

    private void saveOrUpdateFeatureLoadErrorHandler() {
        System.out.println("[Saltr] Feature save or update error.");

    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }


    @Override
    public void addObserver(SaltrObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(SaltrObserver o) {
        observers.remove(o);
    }

    private void appDataLoaded() {
        for (SaltrObserver observer : observers) {
            observer.onGetAppDataSuccess(this);
        }
    }
}
