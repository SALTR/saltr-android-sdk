/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import android.content.ContextWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import saltr.parser.response.AppData;
import saltr.parser.response.SaltrResponse;
import saltr.parser.response.level.LevelData;
import saltr.repository.IRepository;
import saltr.repository.MobileRepository;

import java.text.MessageFormat;
import java.util.*;

public class SLTSaltr {
    private static SLTSaltr saltr;

    protected Gson gson;
    protected IRepository repository;
    protected String saltrUserId;
    protected Boolean isLoading;
    protected Boolean connected;
    protected SLTPartner partner;

    protected String instanceKey;
    protected Map<String, SLTFeature> features;
    protected List<SLTLevelPack> levelPacks;
    protected List<SLTExperiment> experiments;
    protected SLTDevice device;
    protected SLTHttpDataHandler saltrHttpDataHandler;

    private String appVersion;
    private Boolean isInDevMode;

    private SLTSaltr(String instanceKey, ContextWrapper contextWrapper) {
        this.instanceKey = instanceKey;
        isLoading = false;
        connected = false;

        isInDevMode = false;
        features = new HashMap();

        gson = new Gson();
        repository = new MobileRepository(contextWrapper);
    }

    public static SLTSaltr getInstance(String instanceKey, ContextWrapper contextWrapper) {
        if (saltr == null) {
            saltr = new SLTSaltr(instanceKey, contextWrapper);
        }
        return saltr;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Boolean getConnected() {
        return connected;
    }

    public Map<String, SLTFeature> getFeatures() {
        return features;
    }

    public List<SLTLevelPack> getLevelPacks() {
        return levelPacks;
    }

    public List<SLTExperiment> getExperiments() {
        return experiments;
    }

    public SLTFeature getFeature(String token) {
        return features.get(token);
    }

    public void initPartner(String partnerId, String partnerType) {
        this.partner = new SLTPartner(partnerId, partnerType);
    }

    public void initDevice(String deviceId, String deviceType) {
        this.device = new SLTDevice(deviceId, deviceType);
    }

    public void importLevelFromJSON(String json, SLTLevel level) {
        Object data = gson.fromJson(json, Object.class);
        level.updateContent(data);
    }

    public void importLevelPacksFromJSON(String json) {
        Object applicationData = gson.fromJson(json, Object.class);
        levelPacks = SLTDeserializer.decodeLevels(applicationData);
    }

    /**
     * If you want to have a feature synced with SALTR you should call define before getAppData call.
     */
    public void defineFeature(String token, Map<String, String> properties) {
        SLTFeature feature = features.get(token);
        if (feature == null) {
            features.put(token, new SLTFeature(token, null, properties));
        }
        else {
            feature.setDefaultProperties(properties);
        }
    }

    public void getAppData(SLTHttpDataHandler saltrHttpDataHandler) {
        this.saltrHttpDataHandler = saltrHttpDataHandler;
        start();
    }

    private void start() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        connected = false;

        SaltrHttpConnection connection = createAppDataConnection();
        CallBackDetails details = new CallBackDetails(DataType.APP);

        try {
            connection.call(this, details);
        } catch (Exception e) {
            appDataLoadFailedCallback();
        }
    }

    protected void appDataLoadCompleteCallback(String json) {
        System.out.println("[SaltAPI] App data is loaded.");
        SaltrResponse<AppData> response = gson.fromJson(json, new TypeToken<SaltrResponse<AppData>>() {
        }.getType());
        AppData jsonData = response.getResponseData();

        isLoading = false;
        connected = true;
        saltrUserId = jsonData.getSaltId().toString();

        experiments = SLTDeserializer.decodeExperiment(jsonData);
        levelPacks = SLTDeserializer.decodeLevels(jsonData);
        Map<String, SLTFeature> saltrFeatures = SLTDeserializer.decodeFeatures(jsonData);

        //merging with defaults...
        for (Map.Entry<String, SLTFeature> entry : saltrFeatures.entrySet()) {
            SLTFeature saltrFeature = entry.getValue();
            SLTFeature defaultFeature = features.get(entry.getKey());
            if (defaultFeature != null) {
                saltrFeature.setDefaultProperties(defaultFeature.getDefaultProperties());
            }
            features.put(entry.getKey(), saltrFeature);
        }

        System.out.println("[Saltr] packs=" + levelPacks.size());
        saltrHttpDataHandler.onSuccess();

        if (isInDevMode) {
            syncFeatures();
        }
    }

    protected void loadAppDataFailHandler(String msg) {
        saltrHttpDataHandler.onFail();
        this.isLoading = false;
        this.connected = false;
        System.out.println(msg);
    }

    protected void loadAppDataInternal() {
        System.out.println("[SaltClient] NO Internet available - loading internal app data.");
        if (repository == null) {
            loadAppDataFailHandler("[SaltClient] ERROR: Repository is not initialized.");
        }

        Object data = repository.getObjectFromCache(SLTConfig.APP_DATA_URL_CACHE);

        if (data != null) {
            System.out.println("[SaltClient] Loading App data from Cache folder.");
            appDataLoadCompleteCallback((AppData) data);
        }
        else {
            System.out.println("[SaltClient] Loading App data from application folder.");
            data = repository.getObjectFromApplication(SLTConfig.APP_DATA_URL_INTERNAL);
            if (data != null) {
                appDataLoadCompleteCallback((AppData) data);
            }
            else {
                loadAppDataFailHandler("[SaltClient] ERROR: Level Packs are not loaded.");
            }
        }
    }

    public void getLevelDataBody(SLTLevelPack levelPackData, LevelStructure levelData, Boolean useCache, SLTHttpDataHandler saltrHttpDataHandler) {
        this.saltrHttpDataHandler = saltrHttpDataHandler;
        if (!useCache) {
            loadLevelDataFromServer(levelPackData, levelData, true);
            return;
        }

        //if there are no version change than load from cache

        String cachedFileName = MessageFormat.format(SLTConfig.LEVEL_DATA_URL_CACHE_TEMPLATE, levelPackData.getIndex(), levelData.getIndex());
        if (levelData.getVersion().equals(repository.getObjectVersion(cachedFileName))) {
            loadLevelDataCached(levelData, cachedFileName);
        }
        else {
            loadLevelDataFromServer(levelPackData, levelData, false);
        }

    }

    protected void levelLoadSuccessHandler(LevelStructure levelData, LevelData data) {
        levelData.parseData(data);
        saltrHttpDataHandler.onSuccess();

    }

    protected void levelLoadErrorHandler() {
        System.out.println("[SaltClient] ERROR: Level data is not loaded.");
        saltrHttpDataHandler.onFail();
    }

    protected void loadLevelDataLocally(SLTLevelPack levelPackData, LevelStructure levelData, String cachedFileName) {
        if (loadLevelDataCached(levelData, cachedFileName)) {
            return;
        }
        loadLevelDataInternal(levelPackData, levelData);
    }

    protected boolean loadLevelDataCached(LevelStructure levelData, String cachedFileName) {
        System.out.println("[SaltClient::loadLevelData] LOADING LEVEL DATA CACHE IMMEDIATELY.");
        Object object = repository.getObjectFromCache(cachedFileName).toString();
        if (object != null) {
            LevelData data = gson.fromJson(object.toString(), LevelData.class);
            if (data != null) {
                levelLoadSuccessHandler(levelData, data);
                return true;
            }
        }
        return false;
    }

    protected void loadLevelDataFromServer(SLTLevelPack levelPackData, LevelStructure levelData, Boolean forceNoCache) {
        String dataUrl = forceNoCache ? levelData.getDataUrl() + "?_time_=" + new Date().getTime() : levelData.getDataUrl();
        String cachedFileName = MessageFormat.format(SLTConfig.LEVEL_DATA_URL_CACHE_TEMPLATE, levelPackData.getIndex(), levelData.getIndex());

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
            levelLoadSuccessHandler(details.getLevelData(), gson.fromJson(data, LevelData.class));
            repository.cacheObject(details.getCachedFileName(), details.getLevelData().getVersion(), data);
        }
    }

    void levelDataAssetLoadErrorHandler(CallBackDetails details) {
        loadLevelDataLocally(details.getLevelPackData(), details.getLevelData(), details.getCachedFileName());
    }

    private void loadLevelDataInternal(SLTLevelPack levelPackData, LevelStructure levelData) {
        String url = MessageFormat.format(SLTConfig.LEVEL_DATA_URL_LOCAL_TEMPLATE, levelPackData.getIndex(), levelData.getIndex());

        LevelData data = (LevelData) repository.getObjectFromApplication(url);
        if (data != null) {
            levelLoadSuccessHandler(levelData, data);
        }
        else {
            levelLoadErrorHandler();
        }
    }

    void appDataLoadFailedCallback( responseData, details) {
        System.out.println("[Saltr] App data is failed to load.");
        isLoading = false;
        connected = false;
        loadAppDataInternal();
    }

    void appDataAssetLoadCompleteHandler(String data) {
        System.out.println("[SaltAPI] App data is loaded.");
        SaltrResponse<AppData> response = gson.fromJson(data, new TypeToken<SaltrResponse<AppData>>() {
        }.getType());

        if (!response.getStatus().equals(SLTConfig.RESULT_SUCCEED)) {
            loadAppDataInternal();
        }
        else {
            System.out.println("[SaltClient] Loaded App data. json=" + data);
            appDataLoadCompleteCallback(response.getResponseData());
            repository.cacheObject(SLTConfig.APP_DATA_URL_CACHE, "0", response.getResponseData());
        }
    }

    private SaltrHttpConnection createAppDataConnection() {
        Map<String, Object> args = new HashMap<String, Object>();
        if (device != null) {
            args.put("device", device);
        }
        if (partner != null) {
            args.put("partner", partner);
        }
        args.put("instanceKey", instanceKey);

        RequestParams params = new RequestParams();
        params.put("arguments", gson.toJson(args));
        params.put("command", SLTConfig.COMMAND_APP_DATA);

        return new SaltrHttpConnection(SLTConfig.SALTR_API_URL, params);
    }

    public void syncFeatures() {
        List<Map<String, String>> featureList = new ArrayList<Map<String, String>>();
        Map<String, String> tempMap;
        SLTFeature feature;
        for (Map.Entry<String, SLTFeature> entry : features.entrySet()) {
            feature = entry.getValue();
            if (feature.getDefaultProperties() != null) {
                tempMap = new HashMap<String, String>();
                tempMap.put("token", feature.getToken());
                tempMap.put("value", gson.toJson(feature.getDefaultProperties()));
                featureList.add(tempMap);
            }
        }
        RequestParams params = new RequestParams();

        params.put("command", SLTConfig.COMMAND_SAVE_OR_UPDATE_FEATURE);
        params.put("instanceKey", instanceKey);
        params.put("data", gson.toJson(featureList));
        if (appVersion != null) {
            params.put("appVersion", appVersion);
        }

        SaltrHttpConnection connection = new SaltrHttpConnection(SLTConfig.SALTR_URL, params);
        CallBackDetails details = new CallBackDetails(DataType.FEATURE);
        try {
            connection.call(this, details);
        } catch (Exception e) {
        }
    }
}
