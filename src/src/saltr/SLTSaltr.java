/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.content.ContextWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import saltr.game.SLTLevel;
import saltr.game.SLTLevelPack;
import saltr.response.SLTResponse;
import saltr.response.SLTResponseAppData;
import saltr.response.level.SLTResponseLevelData;
import saltr.repository.ISLTRepository;
import saltr.repository.SLTDummyRepository;
import saltr.repository.SLTMobileRepository;
import saltr.status.*;

import java.text.MessageFormat;
import java.util.*;

public class SLTSaltr {
    public static final String CLIENT = "AS3-Mobile";
    public static final String API_VERSION = "1.0.1";
    
    private static SLTSaltr saltr;

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

    private SLTDataHandler saltrHttpDataHandler;

    private boolean devMode;
    private boolean started;
    private boolean useNoLevels;
    private boolean useNoFeatures;
    private String levelType;

    private Gson gson;

    private SLTSaltr(String clientKey, String deviceId, boolean useCache, ContextWrapper contextWrapper) {
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

    public static SLTSaltr getInstance(String clientKey, String deviceId, boolean useCache, ContextWrapper contextWrapper) {
        if (saltr == null) {
            saltr = new SLTSaltr(clientKey, deviceId, useCache, contextWrapper);
        }
        return saltr;
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
        List<SLTLevel> allLevels = new ArrayList<SLTLevel>();
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

    public void connect(SLTDataHandler saltrHttpDataHandler, Object basicProperties, Object customProperties) throws Exception {
        this.saltrHttpDataHandler = saltrHttpDataHandler;
        if (isLoading || !started) {
            return;
        }

        isLoading = true;

        SLTHttpConnection connection = createAppDataConnection(basicProperties, customProperties);
        SLTCallBackProperties details = new SLTCallBackProperties(SLTDataType.APP);

        try {
            connection.call(this, details);
        } catch (Exception e) {
            appDataLoadFailCallback();
        }
    }

    public void loadLevelContent(SLTLevel sltLevel, boolean useCache, SLTDataHandler saltrHttpDataHandler) throws Exception {
        this.saltrHttpDataHandler = saltrHttpDataHandler;
        Object content;
        if (!connected) {
            if (useCache) {
                content = loadLevelContentInternally(sltLevel);
            }
            else {
                content = loadLevelContentFromDisk(sltLevel);
            }
            levelContentLoadSuccessHandler(sltLevel, content);
        }
        else {
            if (!useCache || sltLevel.getVersion().equals(getCachedLevelVersion(sltLevel))) {
                loadLevelContentFromSaltr(sltLevel);
            }
            else {
                content = loadLevelContentFromCache(sltLevel);
                levelContentLoadSuccessHandler(sltLevel, content);
            }
        }
    }

    public void addProperties(Object basicProperties, Object customProperties) throws Exception {
        if (basicProperties == null && customProperties == null || saltrUserId == null) {
            return;
        }

        SLTCallBackProperties details = new SLTCallBackProperties(SLTDataType.PLAYER_PROPERTY);
        SLTHttpConnection connection = createAddPropConnection(basicProperties, customProperties);

        try {
            if (connection != null) {
                connection.call(this, details);
            }
            else {
                System.err.println("error");
            }
        } catch (Exception e) {
            System.err.println("error");
        }
    }

    private SLTHttpConnection createAddPropConnection(Object basicProperties, Object customProperties) throws Exception {
        if (basicProperties != null && customProperties != null || saltrUserId != null) {
            return null;
        }

        Map<String, Object> args = new HashMap<String, Object>();

        args.put("clientKey", clientKey);

        if (deviceId != null) {
            args.put("deviceId", deviceId);
        }
        else {
            throw new Exception("Field 'deviceId' is a required.");
        }

        if (socialId != null) {
            args.put("socialId", socialId);

        }

        if (saltrUserId != null) {
            args.put("saltrUserId", saltrUserId);
        }

        if (basicProperties != null) {
            args.put("basicProperties", basicProperties);
        }

        if (customProperties != null) {
            args.put("customProperties", customProperties);
        }

        RequestParams params = new RequestParams();
        params.put("args", gson.toJson(args));
        params.put("cmd", SLTConfig.CMD_ADD_PROPERTIES);
        params.put("action", SLTConfig.CMD_ADD_PROPERTIES);

        return new SLTHttpConnection(SLTConfig.SALTR_API_URL, params);
    }

    protected void loadLevelContentFromSaltr(SLTLevel level) throws Exception {
        String dataUrl = level.getContentUrl() + "?_time_=" + new Date().getTime();

        SLTCallBackProperties details = new SLTCallBackProperties(SLTDataType.LEVEL);
        details.setLevel(level);

        try {
            SLTHttpConnection connection = new SLTHttpConnection(dataUrl);
            connection.call(this, details);
        } catch (Exception e) {
            loadFromSaltrFailCallback(details);
        }
    }

    void loadFromSaltrSuccessCallback(Object data, SLTCallBackProperties properties) throws Exception {
        if (data != null) {
            cacheLevelContent(properties.getLevel(), data);
        }
        else {
            data = loadLevelContentInternally(properties.getLevel());
        }

        if (data != null) {
            levelContentLoadSuccessHandler(properties.getLevel(), data);
        }
        else {
            levelContentLoadFailHandler();
        }
    }

    void loadFromSaltrFailCallback(SLTCallBackProperties properties) throws Exception {
        Object contentData = loadLevelContentInternally(properties.getLevel());
        levelContentLoadSuccessHandler(properties.getLevel(), gson.fromJson(contentData.toString(), SLTResponseLevelData.class));
    }

    protected void levelContentLoadSuccessHandler(SLTLevel sltLevel, Object content) throws Exception {
        SLTResponseLevelData level = gson.fromJson(content.toString(), SLTResponseLevelData.class);
        sltLevel.updateContent(level);
        saltrHttpDataHandler.onSuccess(this);
    }

    protected void levelContentLoadFailHandler() {
        saltrHttpDataHandler.onFailure(new SLTStatusLevelContentLoadFail());
    }

    private SLTHttpConnection createAppDataConnection(Object basicProperties, Object customProperties) throws Exception {
        Map<String, Object> args = new HashMap<String, Object>();

        args.put("clientKey", clientKey);

        if (deviceId != null) {
            args.put("deviceId", deviceId);
        }
        else {
            throw new Error("Field 'deviceId' is a required.");
        }

        if (socialId != null) {
            args.put("socialId", socialId);
        }

        if (saltrUserId != null) {
            args.put("saltrUserId", saltrUserId);
        }

        if (basicProperties != null) {
            args.put("basicProperties", basicProperties);
        }

        if (customProperties != null) {
            args.put("customProperties", customProperties);
        }


        RequestParams params = new RequestParams();
        params.put("args", gson.toJson(args));
        params.put("cmd", SLTConfig.CMD_APP_DATA);

        return new SLTHttpConnection(SLTConfig.SALTR_API_URL, params);
    }

    void appDataLoadSuccessCallback(String json) throws Exception {
        SLTResponse<SLTResponseAppData> data = gson.fromJson(json, new TypeToken<SLTResponse<SLTResponseAppData>>() {
        }.getType());

        if (data == null) {
            saltrHttpDataHandler.onFailure(new SLTStatusAppDataLoadFail());
            return;
        }

        SLTResponseAppData response = data.getResponseData();
        isLoading = false;

        if (devMode) {
            syncDeveloperFeatures();
        }

        if (data.getStatus().equals(SLTConfig.RESULT_SUCCEED)) {
            Map<String, SLTFeature> saltrFeatures;
            try {
                saltrFeatures = SLTDeserializer.decodeFeatures(response);
            } catch (Exception e) {
                saltrFeatures = null;
                saltrHttpDataHandler.onFailure(new SLTStatusFeaturesParseError());
            }

            try {
                experiments = SLTDeserializer.decodeExperiments(response);
            } catch (Exception e) {
                saltrHttpDataHandler.onFailure(new SLTStatusExperimentsParseError());
            }

            try {
                levelPacks = SLTDeserializer.decodeLevels(response);
            } catch (Exception e) {
                saltrHttpDataHandler.onFailure(new SLTStatusLevelsParseError());
            }

            saltrUserId = response.getSaltrUserId().toString();
            connected = true;
            repository.cacheObject(SLTConfig.APP_DATA_URL_CACHE, "0", response);

            activeFeatures = saltrFeatures;
            saltrHttpDataHandler.onSuccess(this);

            System.out.println("[SALTR] AppData load success. LevelPacks loaded: " + levelPacks.size());
        }
        else {
            saltrHttpDataHandler.onFailure(new SLTStatus(data.getErrorCode(), data.getResponseMessage()));
        }
    }

    void appDataLoadFailCallback() {
        isLoading = false;
        saltrHttpDataHandler.onFailure(new SLTStatusAppDataLoadFail());
    }

    public void syncDeveloperFeatures() throws Exception {
        SLTHttpConnection connection = createSyncFeaturesConnection();
        SLTCallBackProperties details = new SLTCallBackProperties(SLTDataType.FEATURE);
        try {
            connection.call(this, details);
        } catch (Exception e) {
        }
    }

    private SLTHttpConnection createSyncFeaturesConnection() throws Exception {
        List<Map<String, String>> featureList = new ArrayList<Map<String, String>>();
        for (Map.Entry<String, SLTFeature> entry : developerFeatures.entrySet()) {
            Map<String, String> tempMap = new HashMap<String, String>();
            tempMap.put("token", entry.getValue().getToken());
            tempMap.put("value", gson.toJson(entry.getValue().getProperties()));
            featureList.add(tempMap);
        }

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("clientKey", clientKey);
        args.put("developerFeatures", featureList);

        if (deviceId != null) {
            args.put("deviceId", deviceId);
        }
        else {
            throw new Error("Field 'deviceId' is a required.");
        }

        if (socialId != null) {
            args.put("socialId", socialId);
        }

        if (saltrUserId != null) {
            args.put("saltrUserId", saltrUserId);
        }

        RequestParams params = new RequestParams();
        params.put("args", gson.toJson(args));
        params.put("cmd", SLTConfig.CMD_DEV_SYNC_FEATURES);

        return new SLTHttpConnection(SLTConfig.SALTR_URL, params);
    }

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
