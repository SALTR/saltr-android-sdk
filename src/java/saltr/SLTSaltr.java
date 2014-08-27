/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.content.ContextWrapper;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import saltr.exception.*;
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

    public void importLevels(String path) throws SLTException {
        if (!started) {
            path = SLTConfig.LOCAL_LEVELPACK_PACKAGE_URL;
            Object applicationData = repository.getObjectFromApplication(path);
            levelPacks = SLTDeserializer.decodeLevels((SLTResponseAppData) applicationData);
        }
        else {
            throw new SLTException("Method 'importLevels()' should be called before 'start()' only.");
        }
    }

    /**
     * If you want to have a feature synced with SALTR you should call define before getAppData call.
     */
    public void defineFeature(String token, Map<String, String> properties, boolean required) throws SLTException {
        if (!started) {
            developerFeatures.put(token, new SLTFeature(token, properties, required));
        }
        else {
            throw new SLTException("Method 'defineFeature()' should be called before 'start()' only.");
        }
    }

    public void start() throws SLTMissingDeviceIdException, SLTMissingFeaturesException, SLTMissingLevelsException {
        if (deviceId == null) {
            throw new SLTMissingDeviceIdException("deviceId field is required and can't be null.");
        }

        if (developerFeatures.isEmpty() && !useNoFeatures) {
            throw new SLTMissingFeaturesException("Features should be defined.");
        }

        if (levelPacks.isEmpty() && !useNoLevels) {
            throw new SLTMissingLevelsException("Levels should be imported.");
        }

        SLTResponseAppData cachedData = (SLTResponseAppData) repository.getObjectFromCache(SLTConfig.APP_DATA_URL_CACHE);
        if (cachedData == null) {
            for (Map.Entry<String, SLTFeature> entry : developerFeatures.entrySet()) {
                activeFeatures.put(entry.getKey(), entry.getValue());
            }
        }
        else {
            activeFeatures = SLTDeserializer.decodeFeatures(cachedData);
            experiments = SLTDeserializer.decodeExperiments(cachedData);
            saltrUserId = cachedData.getSaltrUserId().toString();
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
        if (deviceId == null) {
            throw new SLTMissingDeviceIdException("device id is missing");
        }
            ApiCall apiCall = new ApiCall();
            apiCall.loadAppData(clientKey, deviceId, socialId, saltrUserId, basicProperties, customProperties,
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

                            Log.i("SALTR", "[SALTR] AppData load success. LevelPacks loaded: " + levelPacks.size());
                        }

                        @Override
                        public void appDataLoadFailCallback() {
                            isLoading = false;
                            appDataHandler.onFailure(new SLTStatusAppDataLoadFail());
                        }
                    }
            );
//        } catch (Exception e) {
//            isLoading = false;
//            appDataHandler.onFailure(new SLTStatusAppDataLoadFail());
//        }
    }

    public void loadLevelContent(SLTLevel sltLevel, boolean useCache, SLTIDataHandler levelDataHandler) {
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
            if (!useCache || !sltLevel.getVersion().equals(getCachedLevelVersion(sltLevel))) {
                loadLevelContentFromSaltr(sltLevel);
            }
            else {
                content = loadLevelContentFromCache(sltLevel);
                levelContentLoadSuccessHandler(sltLevel, gson.fromJson(content.toString(), SLTResponseLevelContentData.class));
            }
        }
    }

    public void addProperties(Object basicProperties, Object customProperties) throws SLTMissingDeviceIdException, SLTNotStartedException {
//        if (deviceId == null) {
//            throw new SLTMissingDeviceIdException("deviceId field is required and can't be null.");
//        }

        if(!started) {
            throw new SLTNotStartedException("");
        }

        if(saltrUserId == null) {
            throw new SLTRuntimeException("saltrUser id is missing");
        }

        ApiCall apiCall = new ApiCall();
        apiCall.addProperties(clientKey, saltrUserId, basicProperties, customProperties);
    }

    protected void loadLevelContentFromSaltr(SLTLevel level) {
        try {
            ApiCall apiCall = new ApiCall();
            apiCall.loadLevelContent(level, new SLTILevelContentDelegate() {
                @Override
                public void loadFromSaltrSuccessCallback(SLTResponseLevelContentData data, SLTLevel sltLevel) {
                    cacheLevelContent(sltLevel, data);
                    levelContentLoadSuccessHandler(sltLevel, data);
                }

                @Override
                public void loadFromSaltrFailCallback(SLTLevel sltLevel) {
                    Object contentData = loadLevelContentInternally(sltLevel);
                    levelContentLoadSuccessHandler(sltLevel, gson.fromJson(contentData.toString(), SLTResponseLevelContentData.class));
                }
            });
        } catch (Exception e) {
            Object contentData = loadLevelContentInternally(level);
            levelContentLoadSuccessHandler(level, gson.fromJson(contentData.toString(), SLTResponseLevelContentData.class));

        }
    }

    protected void levelContentLoadSuccessHandler(SLTLevel sltLevel, SLTResponseLevelContentData level) {
        try {
            sltLevel.updateContent(level);
            levelDataHandler.onSuccess();
        } catch (SLTException e) {
            Log.e("SALTR", e.getMessage());
            levelDataHandler.onFailure(new SLTStatusLevelContentLoadFail());
        }
    }

    private void syncDeveloperFeatures(){
        ApiCall apiCall = new ApiCall();
        apiCall.syncDeveloperFeatures(clientKey, deviceId, socialId, saltrUserId, developerFeatures);
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
