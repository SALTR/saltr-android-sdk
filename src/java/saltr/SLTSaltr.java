/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.content.ContextWrapper;
import android.util.Log;
import com.google.gson.Gson;
import saltr.exception.*;
import saltr.game.SLTLevel;
import saltr.game.SLTLevelPack;
import saltr.repository.ISLTRepository;
import saltr.repository.SLTDummyRepository;
import saltr.repository.SLTRepository;
import saltr.response.SLTResponse;
import saltr.response.SLTResponseAppData;
import saltr.response.level.SLTResponseLevelContentData;
import saltr.status.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class for working with SALTR Android SDK.
 */
public class SLTSaltr {
    public static final String CLIENT = "Android";
    public static final String API_VERSION = "1.0.1";

    private String socialId;
    private String deviceId;
    private boolean connected;
    private String clientKey;
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

    private Gson gson;

    /**
     * The only constructor for SLTSaltr class.
     * @param clientKey (String) This key is specific for SALTR's every application.
     *                  You can find it by logging in to https://saltr.com with your account.
     * @param deviceId (String) this field represents user's device unique ID.
     * @param useCache (boolean) SDK has it's own caching mechanism,
     *                 set it to 'false' if you don't want to use SDK's caching mechanism.
     * @param contextWrapper // TODO do we need this, or client should just send app and cache dir by himself
     */
    public SLTSaltr(String clientKey, String deviceId, boolean useCache, ContextWrapper contextWrapper) {
        this.clientKey = clientKey;
        this.deviceId = deviceId;
        isLoading = false;
        connected = false;
        useNoLevels = false;
        useNoFeatures = false;

        devMode = false;
        started = false;

        activeFeatures = new HashMap<String, SLTFeature>();
        developerFeatures = new HashMap<String, SLTFeature>();
        experiments = new ArrayList<SLTExperiment>();
        levelPacks = new ArrayList<SLTLevelPack>();

        repository = useCache ? new SLTRepository(contextWrapper) : new SLTDummyRepository(contextWrapper);
        gson = new Gson();
    }

    // TODO why do we need this if repo is created based on 'useCache' param value?
    public void setRepository(ISLTRepository repository) {
        this.repository = repository;
    }

    // TODO I don't know what it is for
    public void setUseNoLevels(Boolean useNoLevels) {
        this.useNoLevels = useNoLevels;
    }

    // TODO I don't know what it is for
    public void setUseNoFeatures(Boolean useNoFeatures) {
        this.useNoFeatures = useNoFeatures;
    }

    /**
     * Development mode is used only for development proposes, it enables features like "SALTR feature synchronization".
     * In Live application this mode MUST be disabled.
     * @param devMode (boolean) For turning "development mode" on or off.
     */
    public void setDevMode(Boolean devMode) {
        this.devMode = devMode;
    }

    // TODO I don't know what it is for
    public void setLevelPacks(List<SLTLevelPack> levelPacks) {
        this.levelPacks = levelPacks;
    }

    /**
     * Getting all levels from SDK.
     * @return List of SLTLevel objects.
     */
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

    /**
     * The count of all levels in SDK.
     * @return (int) number of available levels.
     */
    public int getAllLevelsCount() {
        int count = 0;
        for (SLTLevelPack pack : levelPacks) {
            count += pack.getLevels().size();
        }
        return count;
    }

    /**
     * This method is for getting available experiments in which user participate.
     * @return List of SLTExperiment objects.
     */
    public List<SLTExperiment> getExperiments() {
        return experiments;
    }

    /**
     * Client may send to SALTR user ID from third party social networks like Facebook or Google+
     * for better personalization.
     * @param socialId (String) user's ID from social network.
     */
    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    /**
     * Level's in SALTR have two ways of representation:
     * 1. All in one list, where every level has unique index which is designated to call "globalIndex",
     * 2. Levels are grouped in Packs and level's index ("localIndex") is unique only inside that Pack.
     * This method is for getting level if you know his "globalIndex".
     * @param index (int) global index of level in cumulative list of levels.
     * @return object type of SLTLevel type.
     */
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

    /**
     * Level's in SALTR have two ways of representation:
     * 1. All in one list, where every level has unique index which is designated to call "globalIndex",
     * 2. Levels are grouped in Packs and level's index ("localIndex") is unique only inside that Pack.
     * This method is for getting "Pack" if you know level's "globalIndex".
     * @param index (int) global index of level in cumulative list of levels.
     * @return object of SLTPack type.
     */
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

    // TODO I don't know what it is for
    public List<String> getActiveFeatureTokens() {
        List<String> tokens = new ArrayList<String>();
        for (Map.Entry<String, SLTFeature> entry : activeFeatures.entrySet()) {
            tokens.add(entry.getValue().getToken());
        }

        return tokens;
    }

    /**
     * It's for getting the properties of "Feature" object.
     * @param token (String) Special token for "Feature" object which is unique for you SALTR application.
     * @return Map of Objects with String keys.
     */
    public Map<String, Object> getFeatureProperties(String token) {
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

    /**
     * SDK saves all data taken from SALTR into mobile application's storage and whenever you need you can load
     * previously saved levels into SDK.
     * This method should be called before calling SDK's start() method.
     */
    public void importLevels() {
        if (!started) {
            Object applicationData = repository.getObjectFromApplication(SLTConfig.LOCAL_LEVELPACK_PACKAGE_URL);
            levelPacks = SLTDeserializer.decodeLevels((SLTResponseAppData) applicationData);
        }
        else {
            throw new SLTRuntimeException("Method 'importLevels()' should be called before 'start()' only.");
        }
    }

    /**
     * In development mode SDK allows developers to define "SALTR Features" in theirs application on https://saltr.com.
     * This method is for defining that Features that in feature can be synced with SALTR application and
     * should be called before calling SDK's start() method.
     * @param token (String) token for Feature which should be unique for application in SALTR web app.
     * @param properties Map of properties of Feature.
     * @param required Some Features can be required, which means it will always be available for mobile application
     *                 and cannot be disabled or changed from SALTR web interface.
     *                 Only developers has control over required features.
     */
    public void defineFeature(String token, Map<String, Object> properties, boolean required) {
        if (!started) {
            developerFeatures.put(token, new SLTFeature(token, properties, required));
        }
        else {
            throw new SLTRuntimeException("Method 'defineFeature()' should be called before 'start()' only.");
        }
    }

    /**
     * This method is checking if all data is ready for connecting to SALTR web app and if it is,
     * it enables SDK to call connect() method
     */
    public void start() {
        if (deviceId == null) {
            throw new SLTMissingDeviceIdException();
        }

        if (developerFeatures.isEmpty() && !useNoFeatures) {
            throw new SLTMissingFeaturesException();
        }

        if (levelPacks.isEmpty() && !useNoLevels) {
            throw new SLTMissingLevelsException();
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
        }

        started = true;
    }

    /**
     * Connect method tries to connect to SALTR web app and to load application and user specific data.
     * This method should be called after calling start() method.
     * @param appDataHandler Object that has onSuccess() and onFailure() methods that are being called when
     *                       call succeed or failed, respectively.
     * @param basicProperties Basic information about user.
     *                        More information about properties you can find on https://saltr.com/docs#/mobile
     * @param customProperties Custom information about user.
     *                         More information about properties you can find on https://saltr.com/docs#/mobile
     */
    //TODO::@daal why we don't call SLTIDataHandler.onFailure in case if "if (isLoading || !started)". Is this code correct? if we override this.appDataHandler with new one the old handler will be lost
    public void connect(final SLTIDataHandler appDataHandler, Object basicProperties, Object customProperties) {
        this.appDataHandler = appDataHandler;
        if (isLoading || !started) {
            return;
        }

        isLoading = true;
        if (deviceId == null) {
            throw new SLTMissingDeviceIdException();
        }
        SLTApiCall apiCall = new SLTApiCall();
        apiCall.loadAppData(clientKey, deviceId, socialId, basicProperties, customProperties,
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
    }

    /**
     * Loads level's content into provided SLTLevel object.
     * @param sltLevel Object that contains information about level that should be loaded and container for loaded data.
     * @param useCache Specifies whether load cached data or load from SALTR web app.
     * @param levelDataHandler Callback handler object.
     */
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

    /**
     * Method called to send user specific information to SALTR web app.
     * @param basicProperties Basic information about user.
     *                        More information about properties you can find on https://saltr.com/docs#/mobile
     * @param customProperties Custom information about user.
     *                         More information about properties you can find on https://saltr.com/docs#/mobile
     */
    public void addProperties(Object basicProperties, Object customProperties) {
        if (!started) {
            throw new SLTNotStartedException();
        }

        SLTApiCall apiCall = new SLTApiCall();
        apiCall.addProperties(clientKey, socialId, basicProperties, customProperties);
    }

    protected void loadLevelContentFromSaltr(SLTLevel level) {
        try {
            SLTApiCall apiCall = new SLTApiCall();
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

    private void syncDeveloperFeatures() {
        SLTApiCall apiCall = new SLTApiCall();
        apiCall.syncDeveloperFeatures(clientKey, socialId, developerFeatures);
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
