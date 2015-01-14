/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.content.ContextWrapper;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import saltr.exception.*;
import saltr.game.SLTLevel;
import saltr.game.SLTLevelPack;
import saltr.repository.ISLTRepository;
import saltr.repository.SLTDummyRepository;
import saltr.repository.SLTRepository;
import saltr.response.SLTResponseAppData;
import saltr.response.SLTResponseClientData;
import saltr.response.level.SLTResponseLevelContentData;
import saltr.status.SLTStatus;
import saltr.status.SLTStatusAppDataConcurrentLoadRefused;
import saltr.status.SLTStatusLevelContentLoadFail;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The entry point to SDK, and the main class, used to send and receive data from Saltr.
 */
public class SLTSaltr {
    public static final String CLIENT = "Android";
    public static final String API_VERSION = "1.0.0";

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
    private boolean autoRegisterDevice;
    private boolean isSynced;
//    private String levelType;

    private ContextWrapper contextWrapper;
    private Gson gson;

    private int requestIdleTimeout;

    /**
     * Initializes a new instance of the {@link saltr.SLTSaltr} class.
     *
     * @param clientKey      Client key.
     * @param deviceId       Device identifier.
     * @param useCache       If set to <code>true</code> use cache. If not specified defaults to <code>true</code>
     * @param contextWrapper This can be the reference of activity where SDK is initialized.
     */
    public SLTSaltr(String clientKey, String deviceId, boolean useCache, ContextWrapper contextWrapper) {
        this.clientKey = clientKey;
        this.deviceId = deviceId;

        activeFeatures = new HashMap<String, SLTFeature>();
        developerFeatures = new HashMap<String, SLTFeature>();
        experiments = new ArrayList<SLTExperiment>();
        levelPacks = new ArrayList<SLTLevelPack>();
        autoRegisterDevice = true;

        repository = useCache ? new SLTRepository(contextWrapper) : new SLTDummyRepository(contextWrapper);
        gson = new Gson();
        this.contextWrapper = contextWrapper;
        requestIdleTimeout = 3000;
    }

    /**
     * Sets the request idle timeout. If a URL request takes more than timeout to complete, it would be canceled.
     *
     * @param requestIdleTimeout the request idle timeout in milliseconds.
     */
    public void setRequestIdleTimeout(int requestIdleTimeout) {
        this.requestIdleTimeout = requestIdleTimeout;
    }

    /**
     * Sets the repository used by this instance. An appropriate repository is already set by a constructor,
     * so you will need this only if you want to implement and use your own custom repository ({@link saltr.repository.ISLTRepository}).
     *
     * @param repository to set.
     */
    public void setRepository(ISLTRepository repository) {
        this.repository = repository;
    }

    /**
     * Sets a value indicating whether the application does not use levels.
     * @param useNoLevels <code>true</code> if levels are not used; otherwise, <code>false</code>.
     */
    public void setUseNoLevels(Boolean useNoLevels) {
        this.useNoLevels = useNoLevels;
    }

    /**
     * Sets a value indicating whether the application does not use features.
     * @param useNoFeatures <code>true</code> if features are not used, otherwise, <code>false</code>.
     */
    public void setUseNoFeatures(Boolean useNoFeatures) {
        this.useNoFeatures = useNoFeatures;
    }

    /**
     * Sets a value indicating weather this {@link saltr.SLTSaltr} should operate in dev(developer) mode.
     * <p>In this mode client data(e.g. developer defined features) will be synced with Saltr, once, after successful {@link #connect(SLTIDataHandler, Object, Object)} call.</p>
     * @param devMode <code>true</code> to enable; <code>false</code> to disable.
     */
    public void setDevMode(Boolean devMode) {
        this.devMode = devMode;
    }

    /**
     * Sets a value indicating whether device registratioon dialog should be automaticaly shown in dev mode {@link #devMode},
     * after successful <see cref="saltr.SLTUnity.connect"/> call, if the device was not registered already.
     * @param autoRegisterDevice
     */
    public void setAutoRegisterDevice(boolean autoRegisterDevice) {
        this.autoRegisterDevice = autoRegisterDevice;
    }

    /**
     * @return the level packs.
     */
    public List<SLTLevelPack> getLevelPacks() {
        return levelPacks;
    }

    /**
     * @return a list all levels.
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
     * @return the count of all levels.
     */
    public int getAllLevelsCount() {
        int count = 0;
        for (SLTLevelPack pack : levelPacks) {
            count += pack.getLevels().size();
        }
        return count;
    }

    /**
     * @return a list of all experiments.
     */
    public List<SLTExperiment> getExperiments() {
        return experiments;
    }

    /**
     * Sets the social identifier of the user.
     * @param socialId to set.
     */
    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    /**
     * @param index index in all levels.
     * @return a level by its global index in all levels.
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
     * @param index Index of the level in all levels.
     * @return the level pack that contains the level with given global index in all levels.
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

    /**
     * @return a list of tokens(unique identifiers) of all features, active in Saltr.
     */
    public List<String> getActiveFeatureTokens() {
        List<String> tokens = new ArrayList<String>();
        for (Map.Entry<String, SLTFeature> entry : activeFeatures.entrySet()) {
            tokens.add(entry.getValue().getToken());
        }

        return tokens;
    }

    /**
     * Gets the properties of the feature specified by the token.
     * If a feature is set to be required and is not active in, or cannot be retrieved from, Saltr,
     * the properties will be retrieved from default developer defined features.
     * @param token The feature token.
     * @return the properties of the feature.
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
     * Imports the level data from local files, that can be downloaded from Saltr.
     * If your application is using levels, this must be called before calling start(), otherwise has no effect.
     * @param path The path to level packs in Resources folder.
     *             If not specified the {@link saltr.SLTConfig#LOCAL_LEVELPACK_PACKAGE_URL} will be used.
     */
    public void importLevels(String path) {
        if (useNoLevels) {
            return;
        }

        if (!started) {
            if (path == null) {
                path = SLTConfig.LOCAL_LEVELPACK_PACKAGE_URL;
            }
            Object applicationData = repository.getObjectFromApplication(path);
            levelPacks = SLTDeserializer.decodeLevels((SLTResponseAppData) applicationData);
        }
        else {
            throw new SLTRuntimeException("Method 'importLevels()' should be called before 'start()' only.");
        }
    }

    /**
     * Defines a feature {@link saltr.SLTFeature}
     * @param token Token - a unique identifier for the feature.
     * @param properties A Map of properties, that should be of "JSON friendly" data types
     *                   (String, Integer, Double, Map, List, etc.).
     *                   To represent color use standard HTML format: <code>"#RRGGBB"</code>
     * @param required If set to <code>true</code> feature is required(see {@link #getFeatureProperties}). <code>false</code> by default.
     */
    public void defineFeature(String token, Map<String, Object> properties, boolean required) {
        if (useNoFeatures) {
            return;
        }

        if (!started) {
            developerFeatures.put(token, new SLTFeature(token, properties, required));
        }
        else {
            throw new SLTRuntimeException("Method 'defineFeature()' should be called before 'start()' only.");
        }
    }

    /**
     * Checks if everything is initialized properly and starts the instance.
     * <p>After this call you can access application data (levels, features), and establish connection with the server.</p>
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
     * Establishes connection to the server and updates application data(levels, features and experiments).
     * <p>After connecting successfully you can load level content from server with {@link #loadLevelContent(saltr.game.SLTLevel, boolean, SLTIDataHandler)} .</p>
     * @param appDataHandler Instance of class that implements {@link saltr.SLTIDataHandler} interface.
     * @param basicProperties (Optional)Basic properties. Same as in {@link #addProperties(Object, Object)}.
     * @param customProperties (Optional)Basic properties. Same as in {@link #addProperties(Object, Object)}.
     */
    public void connect(final SLTIDataHandler appDataHandler, Object basicProperties, Object customProperties) {
        if (!started) {
            throw new SLTNotStartedException();
        }

        this.appDataHandler = appDataHandler;

        if (isLoading) {
            appDataHandler.onFailure(new SLTStatusAppDataConcurrentLoadRefused());
        }

        isLoading = true;

        SLTAppDataApiCall appDataApiCall = new SLTAppDataApiCall(requestIdleTimeout, devMode, useNoLevels, clientKey, deviceId, socialId, basicProperties, customProperties);
        appDataApiCall.call(
                new SLTIAppDataDelegate() {
                    @Override
                    public void onSuccess(SLTResponseAppData response, Map<String, SLTFeature> responseFeatures, List<SLTExperiment> responseExperiments, List<SLTLevelPack> responseLevels) {
                        isLoading = false;
                        connected = true;
                        activeFeatures = responseFeatures;
                        experiments = responseExperiments;
                        levelPacks = responseLevels;

                        if (devMode && !isSynced) {
                            sync();
//                            isSynced = true;
                        }

                        repository.cacheObject(SLTConfig.APP_DATA_URL_CACHE, "0", response);
                        appDataHandler.onSuccess();
                        Log.i("SALTR", "[SALTR] AppData load success. LevelPacks loaded: " + levelPacks.size());
                    }


                    @Override
                    public void onFailure(SLTStatus status) {
                        isLoading = false;
                        appDataHandler.onFailure(status);
                    }
                });
    }

    /**
     * Loads the content(boards and properties) of the level.
     * <p>Contents may be loaded from server, cache, or local level data({@link #importLevels(String)}).</p>
     * @param sltLevel The level, contents of which will be updated.
     * @param useCache If set to <code>false</code> cached level data will be ignored, forcing content to be loaded from server or local data if connection is not established. <code>true</code> by default.
     * @param levelDataHandler Instance of class that implements {@link saltr.SLTIDataHandler} interface.
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
            if (content != null) {
                levelContentLoadSuccessHandler(sltLevel, content);
            }

        }
        else {
            if (!useCache || !sltLevel.getVersion().equals(getCachedLevelVersion(sltLevel))) {
                loadLevelContentFromSaltr(sltLevel);
            }
            else {
                content = loadLevelContentFromCache(sltLevel);
                levelContentLoadSuccessHandler(sltLevel, content);
            }
        }
    }

    /**
     * Method called to send user specific information to SALTR web app.
     *
     * @param basicProperties  Basic information about user.
     *                         More information about properties you can find on https://saltr.com/docs#/mobile
     * @param customProperties Custom information about user.
     *                         More information about properties you can find on https://saltr.com/docs#/mobile
     */
    public void addProperties(Object basicProperties, Object customProperties) {
        if (!started) {
            throw new SLTNotStartedException();
        }

        SLTAddPropertyApiCall apiCall = new SLTAddPropertyApiCall(requestIdleTimeout, clientKey, socialId, basicProperties, customProperties);
        apiCall.call();
    }

    protected void loadLevelContentFromSaltr(SLTLevel level) {
        try {
            SLTLevelApiCall apiCall = new SLTLevelApiCall(requestIdleTimeout, level);
            apiCall.call(new SLTILevelContentDelegate() {
                @Override
                public void onSuccess(SLTResponseLevelContentData data, SLTLevel sltLevel) {
                    cacheLevelContent(sltLevel, data);
                    levelContentLoadSuccessHandler(sltLevel, data);
                }

                @Override
                public void onFailure(SLTLevel sltLevel) {
                    Object content = loadLevelContentInternally(sltLevel);
                    levelContentLoadSuccessHandler(sltLevel, content);
                }
            });
        } catch (Exception e) {
            Object content = loadLevelContentInternally(level);
            levelContentLoadSuccessHandler(level, content);

        }
    }

    protected void levelContentLoadSuccessHandler(SLTLevel sltLevel, Object content) {
        try {
            SLTResponseLevelContentData level = (SLTResponseLevelContentData) content;
            sltLevel.updateContent(level);
            levelDataHandler.onSuccess();
        } catch (Exception e) {
            Log.e("SALTR", "Couldn't load level");
            levelDataHandler.onFailure(new SLTStatusLevelContentLoadFail());
        }
    }

    void sync() {
        SLTSyncApiCall apiCall = new SLTSyncApiCall(requestIdleTimeout, devMode, clientKey, socialId, deviceId, developerFeatures);
        apiCall.call(new SLTSyncDelegate() {
            @Override
            public void onSuccess(SLTResponseClientData data) {
                if (data.getSuccess()) {
                    isSynced = true;
                }
                else if (data.getError().getCode().equals(SLTStatus.REGISTRATION_REQUIRED_ERROR_CODE) && autoRegisterDevice) {
                    registerDevice();
                }
            }

            @Override
            public void onFailure() {
                Toast toast = Toast.makeText(contextWrapper, "Error occurred during data synchronization", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    /**
     * Opens the device registration dialog. Can be called after {@link #start()} only.
     */
    public void registerDevice() {
        if (!started) {
            throw new SLTRuntimeException("Method 'registerDevice()' should be called after 'start()' only.");
        }
        SLTRegisterDeviceDialogBuilder dialogBuilder = new SLTRegisterDeviceDialogBuilder(contextWrapper);
        dialogBuilder.showDialog(devMode, requestIdleTimeout, clientKey, deviceId, this);
    }

    private String getCachedLevelVersion(SLTLevel sltLevel) {
        String cachedFileName = MessageFormat.format(SLTConfig.LOCAL_LEVEL_CONTENT_CACHE_URL_TEMPLATE, sltLevel.getPackIndex(), sltLevel.getLocalIndex());
        return repository.getObjectVersion(cachedFileName);
    }

    private void cacheLevelContent(SLTLevel sltLevel, SLTResponseLevelContentData contentData) {
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
