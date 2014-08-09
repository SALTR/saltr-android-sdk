/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.content.ContextWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import saltr.game.SLTLevel;
import saltr.game.SLTLevelPack;
import saltr.repository.ISLTRepository;
import saltr.repository.SLTDummyRepository;
import saltr.repository.SLTMobileRepository;
import saltr.response.SLTResponse;
import saltr.response.SLTResponseAppData;
import saltr.response.level.SLTResponseLevelContentData;
import saltr.status.*;

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.*;

//TODO:: @daal Figure out all this exception issues. Also do something with System.out.prints
public class SLTSaltr {
    public static final String CLIENT = "Android";
    public static final String API_VERSION = "1.0.1";

    private String socialId;
    private String deviceId;
    private boolean connected;
    private String clientKey;
    private String saltrUserId;
    private boolean isLoading;

    private ISLTRepository repository;

    private Map<String, SLTFeature> activeFeatures;
    private Map<String, SLTFeature> developerFeatures;

    private List<SLTExperiment> experiments;
    private List<SLTLevelPack> levelPacks;

    private SLTIDataHandler appDataHandler;
    private SLTIDataHandler levelDataHandler;

    private boolean devMode;
    private boolean started;
    private boolean useNoLevels;
    private boolean useNoFeatures;
    private String levelType;

    private Gson gson;

    public SLTSaltr(String clientKey, String deviceId, boolean useCache, ContextWrapper contextWrapper) {
        this.clientKey = clientKey;
        this.deviceId = deviceId;
        isLoading = false;
        connected = false;
        saltrUserId = null;
        useNoLevels = false;
        useNoFeatures = false;

        devMode = false;
        started = false;

        activeFeatures = new HashMap<String, SLTFeature>();
        developerFeatures = new HashMap<String, SLTFeature>();
        experiments = new ArrayList<SLTExperiment>();
        levelPacks = new ArrayList<SLTLevelPack>();

        repository = useCache ? new SLTMobileRepository(contextWrapper) : new SLTDummyRepository(contextWrapper);
        gson = new Gson();
    }

    public void setRepository(ISLTRepository repository) {
        this.repository = repository;
    }

    public void setUseNoLevels(Boolean useNoLevels) {
        this.useNoLevels = useNoLevels;
    }

    public void setUseNoFeatures(Boolean useNoFeatures) {
        this.useNoFeatures = useNoFeatures;
    }

    public void setDevMode(Boolean devMode) {
        this.devMode = devMode;
    }

    public void setLevelPacks(List<SLTLevelPack> levelPacks) {
        this.levelPacks = levelPacks;
    }

    public List<SLTLevel> getAllLevels() {
        List<SLTLevel> allLevels = new ArrayList<>();
        for (SLTLevelPack pack : levelPacks) {
            List<SLTLevel> levels = pack.getLevels();
            for (SLTLevel level : levels) {
                allLevels.add(level);
            }
        }

        return allLevels;
    }

    public int getAllLevelsCount() {
        int count = 0;
        for (SLTLevelPack pack : levelPacks) {
            count += pack.getLevels().size();
        }

        return count;
    }

    public List<SLTExperiment> getExperiments() {
        return experiments;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public SLTLevel getLevelByGlobalIndex(int index) {
        int levelsSum = 0;
        for (SLTLevelPack pack : levelPacks) {
            int packLength = pack.getLevels().size();
            if (index >= levelsSum + packLength) {
                levelsSum += packLength;
            }
            else {
                int localIndex = index - levelsSum;
                return pack.getLevels().get(localIndex);
            }
        }
        return null;
    }

    public SLTLevelPack getPackByLevelGlobalIndex(int index) {
        int levelsSum = 0;
        for (SLTLevelPack pack : levelPacks) {
            int packLength = pack.getLevels().size();
            if (index >= levelsSum + packLength) {
                levelsSum += packLength;
            }
            else {
                return pack;
            }
        }
        return null;
    }

    public List<String> getActiveFeatureTokens() {
        List<String> tokens = new ArrayList<String>();
        for (Map.Entry<String, SLTFeature> entry : activeFeatures.entrySet()) {
            tokens.add(entry.getValue().getToken());
        }

        return tokens;
    }

    public Object getFeatureProperties(String token) {
        SLTFeature activeFeature = activeFeatures.get(token);
        if (activeFeature != null) {
            return activeFeature.getProperties();
        }
        else {
            SLTFeature devFeature = developerFeatures.get(token);
            if (devFeature != null && devFeature.getRequired()) {
                return devFeature.getProperties();
            }
        }

        return null;
    }

    public void importLevels(String path) throws Exception {
        if (started) {
            path = SLTConfig.LOCAL_LEVELPACK_PACKAGE_URL;
            Object applicationData = repository.getObjectFromApplication(path);
            levelPacks = SLTDeserializer.decodeLevels((SLTResponseAppData) applicationData);
        }
        else {
            throw new Exception("Method 'importLevels()' should be called before 'start()' only.");
        }
    }

    /**
     * If you want to have a feature synced with SALTR you should call define before getAppData call.
     */
    public void defineFeature(String token, Map<String, String> properties, boolean required) throws Exception {
        if (!started) {
            developerFeatures.put(token, new SLTFeature(token, properties, required));
        }
        else {
            throw new Exception("Method 'defineFeature()' should be called before 'start()' only.");
        }
    }

    public void start() throws Exception {
        if (deviceId == null) {
            throw new Exception("deviceId field is required and can't be null.");
        }

        if (developerFeatures.isEmpty() && !useNoFeatures) {
            throw new Exception("Features should be defined.");
        }

        if (levelPacks.isEmpty() && !useNoLevels) {
            throw new Exception("Levels should be imported.");
        }

        Object cachedData = repository.getObjectFromCache(SLTConfig.APP_DATA_URL_CACHE);
        if (cachedData == null) {
            for (Map.Entry<String, SLTFeature> entry : developerFeatures.entrySet()) {
                activeFeatures.put(entry.getKey(), entry.getValue());
            }
        }
        else {
            activeFeatures = SLTDeserializer.decodeFeatures((SLTResponseAppData) cachedData);
            experiments = SLTDeserializer.decodeExperiments((SLTResponseAppData) cachedData);
            saltrUserId = ((SLTResponseAppData) cachedData).getSaltrUserId().toString();
        }

        started = true;
    }

    //TODO::@daal why we don't call SLTIDataHandler.onFailure in case if "if (isLoading || !started)". Is this code correct? if we override this.appDataHandler with new one the old handler will be lost
    public void connect(final SLTIDataHandler appDataHandler, Object basicProperties, Object customProperties) {
        this.appDataHandler = appDataHandler;
        if (isLoading || !started) {
            return;
        }

        isLoading = true;

        try {
            ApiCall apiCall = new ApiCall();
            if(deviceId == null) {
                throw new Exception("deviceId field is required and can't be null.");
            }
            apiCall.loadAppData(clientKey,deviceId,socialId,saltrUserId,basicProperties,customProperties,
                    new SLTIAppDataDelegate() {
                        @Override
                        public void appDataLoadSuccessCallback(SLTResponse<SLTResponseAppData> data) {
                            SLTResponseAppData response = data.getResponseData();
                            isLoading = false;

                            if (devMode) {
                                syncDeveloperFeatures();
                            }

                            Map<String, SLTFeature> saltrFeatures;
                            try {
                                saltrFeatures = SLTDeserializer.decodeFeatures(response);
                            } catch (Exception e) {
                                saltrFeatures = null;
                                appDataHandler.onFailure(new SLTStatusFeaturesParseError());
                            }

                            try {
                                experiments = SLTDeserializer.decodeExperiments(response);
                            } catch (Exception e) {
                                appDataHandler.onFailure(new SLTStatusExperimentsParseError());
                            }

                            try {
                                levelPacks = SLTDeserializer.decodeLevels(response);
                            } catch (Exception e) {
                                appDataHandler.onFailure(new SLTStatusLevelsParseError());
                            }

                            saltrUserId = response.getSaltrUserId().toString();
                            connected = true;
                            repository.cacheObject(SLTConfig.APP_DATA_URL_CACHE, "0", response);

                            activeFeatures = saltrFeatures;
                            appDataHandler.onSuccess();

                            System.out.println("[SALTR] AppData load success. LevelPacks loaded: " + levelPacks.size());
                        }

                        @Override
                        public void appDataLoadFailCallback() {
                            isLoading = false;
                            appDataHandler.onFailure(new SLTStatusAppDataLoadFail());
                        }
                    }
                    );
        } catch (Exception e) {
            isLoading = false;
            appDataHandler.onFailure(new SLTStatusAppDataLoadFail());

        }
    }

    public void loadLevelContent(SLTLevel sltLevel, boolean useCache, SLTIDataHandler levelDataHandler) throws Exception {
        this.levelDataHandler = levelDataHandler;
        Object content;
        if (!connected) {
            if (useCache) {
                content = loadLevelContentInternally(sltLevel);
            }
            else {
                content = loadLevelContentFromDisk(sltLevel);
            }
            levelContentLoadSuccessHandler(sltLevel, gson.fromJson(content.toString(), SLTResponseLevelContentData.class));
        }
        else {
            //TODO::@daal sltLevel.getVersion().equals(getCachedLevelVersion(sltLevel)) ??? Or not equals?
            if (!useCache || sltLevel.getVersion().equals(getCachedLevelVersion(sltLevel))) {
                loadLevelContentFromSaltr(sltLevel);
            }
            else {
                content = loadLevelContentFromCache(sltLevel);
                levelContentLoadSuccessHandler(sltLevel, gson.fromJson(content.toString(), SLTResponseLevelContentData.class));
            }
        }
    }

    //TODO:: @daal why we throw Exception type exception? Let's have some SLTException
    public void addProperties(Object basicProperties, Object customProperties) throws Exception {
        if (basicProperties == null && customProperties == null || saltrUserId == null) {
            return;
        }

        if (deviceId == null) {
            throw new Exception("deviceId field is required and can't be null.");
        }

        ApiCall apiCall = new ApiCall();
        apiCall.addProperties(clientKey,deviceId,socialId,saltrUserId,basicProperties, customProperties);

//        SLTHttpsConnection connection = createPlayerPropertiesConnection(basicProperties, customProperties);
//        try {
//            //TODO:: @daal  Can connection be null?
//            if (connection != null) {
//                connection.execute(this);
//            }
//        } catch (Exception e) {
//            System.err.println("error");
//        }
    }

    //TODO:: @daal why we throw Exception type exception? What if we will have one more high level class for building SLTHttpsConnections and executing them?
//    private SLTHttpsConnection createPlayerPropertiesConnection(Object basicProperties, Object customProperties) throws MalformedURLException, Exception  {
//        Map<String, Object> args = new HashMap<>();
//
//        args.put("clientKey", clientKey);
//
//        if (deviceId != null) {
//            args.put("deviceId", deviceId);
//        }
//        else {
//            throw new Exception("Field 'deviceId' is a required.");
//        }
//
//        if (socialId != null) {
//            args.put("socialId", socialId);
//
//        }
//
//        if (saltrUserId != null) {
//            args.put("saltrUserId", saltrUserId);
//        }
//
//        if (basicProperties != null) {
//            args.put("basicProperties", basicProperties);
//        }
//
//        if (customProperties != null) {
//            args.put("customProperties", customProperties);
//        }
//
//        SLTCallBackProperties props = new SLTCallBackProperties(SLTDataType.PLAYER_PROPERTY);
//        SLTHttpsConnection connection = new SLTHttpsConnection();
//
//        connection.setParameters("args", gson.toJson(args));
//        connection.setParameters("cmd", SLTConfig.CMD_ADD_PROPERTIES);
//        connection.setParameters("action", SLTConfig.CMD_ADD_PROPERTIES);
//
//        connection.setUrl(SLTConfig.SALTR_API_URL);
//
//        return connection;
//    }


    protected void loadLevelContentFromSaltr(SLTLevel level) throws Exception {
        try {
            ApiCall apiCall = new ApiCall();
            apiCall.loadLevelContent(level, new SLTILevelContentDelegate() {
                @Override
                public void loadFromSaltrSuccessCallback(SLTResponseLevelContentData data, SLTLevel sltLevel) {
                    cacheLevelContent(sltLevel, data);
                    try {
                        levelContentLoadSuccessHandler(sltLevel, data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void loadFromSaltrFailCallback(SLTLevel sltLevel) {
                    Object contentData = loadLevelContentInternally(sltLevel);
                    try {
                        levelContentLoadSuccessHandler(sltLevel, gson.fromJson(contentData.toString(), SLTResponseLevelContentData.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Object contentData = loadLevelContentInternally(level);
            levelContentLoadSuccessHandler(level, gson.fromJson(contentData.toString(), SLTResponseLevelContentData.class));

        }
    }

//    void loadFromSaltrSuccessCallback(Object data, SLTLevel sltLevel) throws Exception {
//        if (data != null) {
//            cacheLevelContent(sltLevel, data);
//        }
//        else {
//            data = loadLevelContentInternally(sltLevel);
//        }
//
//        if (data != null) {
//            levelContentLoadSuccessHandler(sltLevel, data);
//        }
//        else {
//            levelContentLoadFailHandler();
//        }
//    }

//    void loadFromSaltrFailCallback(SLTLevel sltLevel) throws Exception {
//        Object contentData = loadLevelContentInternally(sltLevel);
//        levelContentLoadSuccessHandler(sltLevel, gson.fromJson(contentData.toString(), SLTResponseLevelContentData.class));
//    }

    //TODO:: @daal why this method throws exception? Isn't that better to catch it here and call fail calback?
    protected void levelContentLoadSuccessHandler(SLTLevel sltLevel, SLTResponseLevelContentData level) throws Exception {
        sltLevel.updateContent(level);
        levelDataHandler.onSuccess();
    }

//    protected void levelContentLoadFailHandler() {
//        levelDataHandler.onFailure(new SLTStatusLevelContentLoadFail());
//    }

//    private SLTHttpsConnection createAppDataConnection(Object basicProperties, Object customProperties) throws MalformedURLException, Exception {
//        Map<String, Object> args = new HashMap<String, Object>();
//
//        args.put("clientKey", clientKey);
//
//        if (deviceId != null) {
//            args.put("deviceId", deviceId);
//        }
//        else {
//            throw new Error("Field 'deviceId' is a required.");
//        }
//
//        if (socialId != null) {
//            args.put("socialId", socialId);
//        }
//
//        if (saltrUserId != null) {
//            args.put("saltrUserId", saltrUserId);
//        }
//
//        if (basicProperties != null) {
//            args.put("basicProperties", basicProperties);
//        }
//
//        if (customProperties != null) {
//            args.put("customProperties", customProperties);
//        }
//
//        SLTCallBackProperties props = new SLTCallBackProperties(SLTDataType.APP);
//        SLTHttpsConnection connection = new SLTHttpsConnection(appDataHandler, props);
//
//        connection.setParameters("args", gson.toJson(args));
//        connection.setParameters("cmd", SLTConfig.CMD_APP_DATA);
//        connection.setParameters("action", SLTConfig.CMD_APP_DATA);
//
//        connection.setUrl(SLTConfig.SALTR_API_URL);
//
//        return connection;
//    }

//    void appDataLoadSuccessCallback(String json) throws Exception {
//        SLTResponse<SLTResponseAppData> data = gson.fromJson(json, new TypeToken<SLTResponse<SLTResponseAppData>>() {
//        }.getType());
//
//        if (data == null) {
//            appDataHandler.onFailure(new SLTStatusAppDataLoadFail());
//            return;
//        }
//
//        SLTResponseAppData response = data.getResponseData();
//        isLoading = false;
//
//        if (devMode) {
//            syncDeveloperFeatures();
//        }
//
//        if (data.getStatus().equals(SLTConfig.RESULT_SUCCEED)) {
//            Map<String, SLTFeature> saltrFeatures;
//            try {
//                saltrFeatures = SLTDeserializer.decodeFeatures(response);
//            } catch (Exception e) {
//                saltrFeatures = null;
//                appDataHandler.onFailure(new SLTStatusFeaturesParseError());
//            }
//
//            try {
//                experiments = SLTDeserializer.decodeExperiments(response);
//            } catch (Exception e) {
//                appDataHandler.onFailure(new SLTStatusExperimentsParseError());
//            }
//
//            try {
//                levelPacks = SLTDeserializer.decodeLevels(response);
//            } catch (Exception e) {
//                appDataHandler.onFailure(new SLTStatusLevelsParseError());
//            }
//
//            saltrUserId = response.getSaltrUserId().toString();
//            connected = true;
//            repository.cacheObject(SLTConfig.APP_DATA_URL_CACHE, "0", response);
//
//            activeFeatures = saltrFeatures;
//            appDataHandler.onSuccess();
//
//            System.out.println("[SALTR] AppData load success. LevelPacks loaded: " + levelPacks.size());
//        }
//        else {
//            appDataHandler.onFailure(new SLTStatus(data.getErrorCode(), data.getResponseMessage()));
//        }
//    }

//    void appDataLoadFailCallback() {
//        isLoading = false;
//        appDataHandler.onFailure(new SLTStatusAppDataLoadFail());
//    }

    public void syncDeveloperFeatures() {
        try {
            if (deviceId == null) {
                throw new Error("Field 'deviceId' is a required.");
            }

            ApiCall apiCall = new ApiCall();
            apiCall.syncDeveloperFeatures(clientKey,deviceId,socialId,saltrUserId,developerFeatures);
        } catch (Exception e) {
        }
    }

//    private SLTHttpsConnection createSyncFeaturesConnection() throws MalformedURLException, Exception {
//        List<Map<String, String>> featureList = new ArrayList<Map<String, String>>();
//        for (Map.Entry<String, SLTFeature> entry : developerFeatures.entrySet()) {
//            Map<String, String> tempMap = new HashMap<String, String>();
//            tempMap.put("token", entry.getValue().getToken());
//            tempMap.put("value", gson.toJson(entry.getValue().getProperties()));
//            featureList.add(tempMap);
//        }
//
//        Map<String, Object> args = new HashMap<String, Object>();
//        args.put("clientKey", clientKey);
//        args.put("developerFeatures", featureList);
//
//        if (deviceId != null) {
//            args.put("deviceId", deviceId);
//        }
//        else {
//            throw new Error("Field 'deviceId' is a required.");
//        }
//
//        if (socialId != null) {
//            args.put("socialId", socialId);
//        }
//
//        if (saltrUserId != null) {
//            args.put("saltrUserId", saltrUserId);
//        }
//
//        SLTCallBackProperties props = new SLTCallBackProperties(SLTDataType.FEATURE);
//        SLTHttpsConnection connection = new SLTHttpsConnection(new SLTIDataHandler() {
//
//            @Override
//            public void onSuccess() {
//                System.out.println("[Saltr] Dev feature Sync is complete.");
//            }
//
//            @Override
//            public void onFailure(SLTStatus status) {
//                System.out.println("[Saltr] Dev feature Sync has failed.");
//            }
//        }, props);
//
//
//        connection.setParameters("args", gson.toJson(args));
//        connection.setParameters("cmd", SLTConfig.CMD_DEV_SYNC_FEATURES);
//        connection.setParameters("action", SLTConfig.CMD_DEV_SYNC_FEATURES);
//
//        connection.setUrl(SLTConfig.SALTR_DEVAPI_URL);
//
//        return connection;
//    }

    private String getCachedLevelVersion(SLTLevel sltLevel) {
        String cachedFileName = MessageFormat.format(SLTConfig.LOCAL_LEVEL_CONTENT_CACHE_URL_TEMPLATE, sltLevel.getPackIndex(), sltLevel.getLocalIndex());
        return repository.getObjectVersion(cachedFileName);
    }

    private void cacheLevelContent(SLTLevel sltLevel, Object contentData) {
        String cachedFileName = MessageFormat.format(SLTConfig.LOCAL_LEVEL_CONTENT_CACHE_URL_TEMPLATE, sltLevel.getPackIndex(), sltLevel.getLocalIndex());
        repository.cacheObject(cachedFileName, sltLevel.getVersion(), contentData);
    }

    private Object loadLevelContentInternally(SLTLevel sltLevel) {
        Object content = loadLevelContentFromCache(sltLevel);
        if (content == null) {
            content = loadLevelContentFromDisk(sltLevel);
        }
        return content;
    }

    private Object loadLevelContentFromCache(SLTLevel sltLevel) {
        String url = MessageFormat.format(SLTConfig.LOCAL_LEVEL_CONTENT_CACHE_URL_TEMPLATE, sltLevel.getPackIndex(), sltLevel.getLocalIndex());
        return repository.getObjectFromCache(url);
    }

    private Object loadLevelContentFromDisk(SLTLevel sltLevel) {
        String url = MessageFormat.format(SLTConfig.LOCAL_LEVEL_CONTENT_PACKAGE_URL_TEMPLATE, sltLevel.getPackIndex(), sltLevel.getLocalIndex());
        return repository.getObjectFromApplication(url);
    }
}
