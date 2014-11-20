/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import saltr.response.SLTResponseTemplate;
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
 * Main class for working with SALTR Android SDK.
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
//    private String levelType;

    private ContextWrapper contextWrapper;
    private Gson gson;

    private int timeout;

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * The only constructor for SLTSaltr class.
     *
     * @param clientKey      (String) This key is specific for SALTR's every application.
     *                       You can find it by logging in to https://saltr.com with your account.
     * @param deviceId       (String) this field represents user's device unique ID.
     * @param useCache       (boolean) SDK has it's own caching mechanism,
     *                       set it to 'false' if you don't want to use SDK's caching mechanism.
     * @param contextWrapper // TODO do we need this, or client should just send app and cache dir by himself
     */
    public SLTSaltr(String clientKey, String deviceId, boolean useCache, ContextWrapper contextWrapper) {
        this.clientKey = clientKey;
        this.deviceId = deviceId;

        activeFeatures = new HashMap<String, SLTFeature>();
        developerFeatures = new HashMap<String, SLTFeature>();
        experiments = new ArrayList<SLTExperiment>();
        levelPacks = new ArrayList<SLTLevelPack>();

        repository = useCache ? new SLTRepository(contextWrapper) : new SLTDummyRepository(contextWrapper);
        gson = new Gson();
        this.contextWrapper = contextWrapper;
        timeout = 0;
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

    /**
     * Development mode is used only for development proposes, it enables features like "SALTR feature synchronization".
     * In Live application this mode MUST be disabled.
     *
     * @param devMode (boolean) For turning "development mode" on or off.
     */
    public void setDevMode(Boolean devMode) {
        this.devMode = devMode;
    }

    public void setLevelPacks(List<SLTLevelPack> levelPacks) {
        this.levelPacks = levelPacks;
    }

    /**
     * Getting all levels from SDK.
     *
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
     *
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
     *
     * @return List of SLTExperiment objects.
     */
    public List<SLTExperiment> getExperiments() {
        return experiments;
    }

    /**
     * Client may send to SALTR user ID from third party social networks like Facebook or Google+
     * for better personalization.
     *
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
     *
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
     *
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

    public List<String> getActiveFeatureTokens() {
        List<String> tokens = new ArrayList<String>();
        for (Map.Entry<String, SLTFeature> entry : activeFeatures.entrySet()) {
            tokens.add(entry.getValue().getToken());
        }

        return tokens;
    }

    /**
     * It's for getting the properties of "Feature" object.
     *
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
     * In development mode SDK allows developers to define "SALTR Features" in theirs application on https://saltr.com.
     * This method is for defining that Features that in feature can be synced with SALTR application and
     * should be called before calling SDK's start() method.
     *
     * @param token      (String) token for Feature which should be unique for application in SALTR web app.
     * @param properties Map of properties of Feature.
     * @param required   Some Features can be required, which means it will always be available for mobile application
     *                   and cannot be disabled or changed from SALTR web interface.
     *                   Only developers has control over required features.
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
     *
     * @param appDataHandler   Object that has onSuccess() and onFailure() methods that are being called when
     *                         call succeed or failed, respectively.
     * @param basicProperties  Basic information about user.
     *                         More information about properties you can find on https://saltr.com/docs#/mobile
     * @param customProperties Custom information about user.
     *                         More information about properties you can find on https://saltr.com/docs#/mobile
     */
    //TODO::@daal why we don't call SLTIDataHandler.onFailure in case if "if (isLoading || !started)". Is this code correct? if we override this.appDataHandler with new one the old handler will be lost
    public void connect(final SLTIDataHandler appDataHandler, Object basicProperties, Object customProperties) {
        if (!started) {
            throw new SLTNotStartedException();
        }

        this.appDataHandler = appDataHandler;

        if (isLoading) {
            appDataHandler.onFailure(new SLTStatusAppDataConcurrentLoadRefused());
        }

        isLoading = true;

        SLTAppDataApiCall appDataApiCall = new SLTAppDataApiCall(timeout, devMode, useNoLevels, clientKey, deviceId, socialId, basicProperties, customProperties);
        appDataApiCall.call(
                new SLTIAppDataDelegate() {
                    @Override
                    public void onSuccess(SLTResponseAppData response, Map<String, SLTFeature> responseFeatures, List<SLTExperiment> responseExperiments, List<SLTLevelPack> responseLevels) {
                        isLoading = false;
                        connected = true;
                        activeFeatures = responseFeatures;
                        experiments = responseExperiments;
                        levelPacks = responseLevels;

                        if (devMode) {
                            sync();
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
     * Loads level's content into provided SLTLevel object.
     *
     * @param sltLevel         Object that contains information about level that should be loaded and container for loaded data.
     * @param useCache         Specifies whether load cached data or load from SALTR web app.
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

        SLTAddPropertyApiCall apiCall = new SLTAddPropertyApiCall(timeout, clientKey, socialId, basicProperties, customProperties);
        apiCall.call();
    }

    protected void loadLevelContentFromSaltr(SLTLevel level) {
        try {
            SLTLevelApiCall apiCall = new SLTLevelApiCall(timeout, level);
            apiCall.call(new SLTILevelContentDelegate() {
                @Override
                public void onSuccess(SLTResponseLevelContentData data, SLTLevel sltLevel) {
                    cacheLevelContent(sltLevel, data);
                    levelContentLoadSuccessHandler(sltLevel, data);
                }

                @Override
                public void onFailure(SLTLevel sltLevel) {
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

    private void sync() {
        SLTSyncApiCall apiCall = new SLTSyncApiCall(timeout, devMode, clientKey, socialId, deviceId, developerFeatures);
        apiCall.call(new SLTSyncDelegate() {
            @Override
            public void onSuccess(SLTResponseClientData data) {
                if (data.getRegistrationRequired()) {

                    final EditText name = new EditText(contextWrapper);
                    final EditText email = new EditText(contextWrapper);
                    name.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    email.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    name.setLines(1);
                    email.setLines(1);
                    name.setHint("device name");
                    email.setHint("myemail@example.com");
                    LinearLayout layout = new LinearLayout(contextWrapper);
                    layout.addView(email);
                    layout.addView(name);
                    layout.setOrientation(1);

                    final AlertDialog dialog = new AlertDialog.Builder(contextWrapper)
                            .setTitle("Register device")
                            .setView(layout)
                            .setPositiveButton("Ok", null)
                            .setNegativeButton("Cancel", null).create();

                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            Button ok = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Editable editableName = name.getText();
                                    Editable editableEmail = email.getText();

                                    SLTAddDeviceToSaltrApiCall apiCall = new SLTAddDeviceToSaltrApiCall(timeout, devMode, editableName.toString(), editableEmail.toString(), clientKey, deviceId);
                                    apiCall.call(new SLTAddDeviceDelegate() {
                                        @Override
                                        public void onSuccess(SLTResponseTemplate response) {
                                            if (response.getSuccess()) {
                                                dialog.dismiss();
                                            }
                                            else {
                                                Toast toast = Toast.makeText(contextWrapper, response.getError().getMessage(), Toast.LENGTH_LONG);
                                                toast.show();
                                            }
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast toast = Toast.makeText(contextWrapper, "Error", Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                    });
                                }
                            });
                            Button cancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                            cancel.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onFailure() {
                Toast toast = Toast.makeText(contextWrapper, "Error occurred during data synchronization", Toast.LENGTH_LONG);
                toast.show();
            }
        });
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
