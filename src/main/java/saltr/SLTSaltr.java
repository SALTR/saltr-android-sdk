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
import saltr.parser.game.SLTLevel;
import saltr.parser.game.SLTLevelPack;
import saltr.parser.response.AppData;
import saltr.parser.response.SaltrResponse;
import saltr.parser.response.level.LevelData;
import saltr.repository.ISLTRepository;
import saltr.repository.SLTMobileRepository;

import java.text.MessageFormat;
import java.util.*;

public class SLTSaltr {
    private static SLTSaltr saltr;

    protected Gson gson;
    protected ISLTRepository repository;
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
        features = new HashMap<>();

        gson = new Gson();
        repository = new SLTMobileRepository(contextWrapper);
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

    public void importLevels(String path) {
        if (path == null) {
            path = SLTConfig.LEVEL_PACK_URL_PACKAGE;
        }
        Object applicationData = repository.getObjectFromApplication(path);
        levelPacks = SLTDeserializer.decodeLevels((AppData) applicationData);
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

    public void start(SLTHttpDataHandler saltrHttpDataHandler) {
        this.saltrHttpDataHandler = saltrHttpDataHandler;
        if (isLoading) {
            return;
        }
        applyCachedFeatures();

        isLoading = true;
        connected = false;
        SLTHttpConnection connection = createAppDataConnection();
        SLTCallBackProperties details = new SLTCallBackProperties(SLTDataType.APP);

        try {
            connection.call(this, details);
        } catch (Exception e) {
            appDataLoadFailedCallback();
        }
    }

    private void applyCachedFeatures() {
        Object cachedData = repository.getObjectFromCache(SLTConfig.APP_DATA_URL_CACHE);
        if (cachedData == null) {
            return;
        }
        Map<String, SLTFeature> cachedFeatures = SLTDeserializer.decodeFeatures((AppData) cachedData);
        for (Map.Entry<String, SLTFeature> entry : cachedFeatures.entrySet()) {
            String token = entry.getKey();
            SLTFeature saltrFeature = entry.getValue();
            SLTFeature defaultFeature = features.get(token);
            if (defaultFeature != null) {
                saltrFeature.setDefaultProperties(defaultFeature.getDefaultProperties());
            }
            features.put(token, saltrFeature);
        }
    }

    void appDataLoadFailedCallback() {
        System.out.println("[Saltr] App data is failed to load.");
        isLoading = false;
        connected = false;
        saltrHttpDataHandler.onFail();
    }

    protected void appDataLoadCompleteCallback(String json) {
        SaltrResponse<AppData> response = gson.fromJson(json, new TypeToken<SaltrResponse<AppData>>() {
        }.getType());
        AppData responseData = response.getResponseData();
        isLoading = false;
        if (response.getStatus().equals(SLTConfig.RESULT_SUCCEED)) {
            repository.cacheObject(SLTConfig.APP_DATA_URL_CACHE, "0", responseData);
            connected = true;
            if (responseData.getSaltId() != null) {
                saltrUserId = responseData.getSaltId().toString();
            }
            else {
                saltrUserId = responseData.getSaltrUserId().toString();
            }
            experiments = SLTDeserializer.decodeExperiment(responseData);
            levelPacks = SLTDeserializer.decodeLevels(responseData);
            Map<String, SLTFeature> saltrFeatures = SLTDeserializer.decodeFeatures(responseData);

            //merging with defaults...
            for (Map.Entry<String, SLTFeature> entry : saltrFeatures.entrySet()) {
                String token = entry.getKey();
                SLTFeature saltrFeature = entry.getValue();
                SLTFeature defaultFeature = features.get(token);
                if (defaultFeature != null) {
                    saltrFeature.setDefaultProperties(defaultFeature.getDefaultProperties());
                }
                features.put(token, saltrFeature);
            }

            System.out.println("[Saltr] packs=" + levelPacks.size());
            saltrHttpDataHandler.onSuccess();

            if (isInDevMode) {
                syncFeatures();
            }
        }
        else {
            connected = false;
            saltrHttpDataHandler.onFail();
        }
    }

    private SLTHttpConnection createAppDataConnection() {
        Map<String, Object> args = new HashMap<>();
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

        return new SLTHttpConnection(SLTConfig.SALTR_API_URL, params);
    }

    public void syncFeatures() {
        List<Map<String, String>> featureList = new ArrayList<>();
        Map<String, String> tempMap;
        SLTFeature feature;
        for (Map.Entry<String, SLTFeature> entry : features.entrySet()) {
            feature = entry.getValue();
            if (feature.getDefaultProperties() != null) {
                tempMap = new HashMap<>();
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

        SLTHttpConnection connection = new SLTHttpConnection(SLTConfig.SALTR_URL, params);
        SLTCallBackProperties details = new SLTCallBackProperties(SLTDataType.FEATURE);
        try {
            connection.call(this, details);
        } catch (Exception e) {
        }
    }

    // level content data loading methods.
    public void loadLevelContentData(SLTLevelPack levelPack, SLTLevel level, boolean useCache, SLTHttpDataHandler saltrHttpDataHandler) {
        this.saltrHttpDataHandler = saltrHttpDataHandler;
        if (!useCache) {
            loadSaltrLevelContentData(levelPack, level, true);
        }
        else {
            //if there are no version change than load from cache
            String cachedVersion = getCachedLevelVersion(levelPack, level);
            if (level.getVersion().equals(cachedVersion)) {
                Object contentData = loadCachedLevelContentData(levelPack, level);
                contentDataLoadSuccessCallback(level, gson.fromJson(contentData.toString(), LevelData.class));
            }
            else {
                loadSaltrLevelContentData(levelPack, level, false);
            }
        }
    }

    private String getCachedLevelVersion(SLTLevelPack levelPack, SLTLevel level) {
        String cachedFileName = MessageFormat.format(SLTConfig.LEVEL_CONTENT_DATA_URL_CACHE_TEMPLATE, levelPack.getIndex(), level.getIndex());
        return repository.getObjectVersion(cachedFileName);
    }


    private void cacheLevelContentData(SLTLevelPack levelPack, SLTLevel level, Object contentData) {
        String cachedFileName = MessageFormat.format(SLTConfig.LEVEL_CONTENT_DATA_URL_CACHE_TEMPLATE, levelPack.getIndex(), level.getIndex());
        repository.cacheObject(cachedFileName, level.getVersion(), contentData);
    }

    private Object loadLevelContentDataInternally(SLTLevelPack levelPack, SLTLevel level) {
        Object contentData = loadCachedLevelContentData(levelPack, level);
        if (contentData == null) {
            contentData = loadDefaultLevelContentData(levelPack, level);
        }
        return contentData;
    }

    private Object loadCachedLevelContentData(SLTLevelPack levelPack, SLTLevel level) {
        String url = MessageFormat.format(SLTConfig.LEVEL_CONTENT_DATA_URL_CACHE_TEMPLATE, levelPack.getIndex(), level.getIndex());
        return repository.getObjectFromCache(url);
    }

    private Object loadDefaultLevelContentData(SLTLevelPack levelPack, SLTLevel level) {
        String url = MessageFormat.format(SLTConfig.LEVEL_CONTENT_DATA_URL_PACKAGE_TEMPLATE, levelPack.getIndex(), level.getIndex());
        return repository.getObjectFromApplication(url);
    }

    protected void loadSaltrLevelContentData(SLTLevelPack levelPack, SLTLevel level, Boolean forceNoCache) {
        String dataUrl = forceNoCache ? level.getContentDataUrl() + "?_time_=" + new Date().getTime() : level.getContentDataUrl();

        SLTCallBackProperties details = new SLTCallBackProperties(SLTDataType.LEVEL);
        details.setLevel(level);
        details.setPack(levelPack);

        try {
            SLTHttpConnection connection = new SLTHttpConnection(dataUrl);
            connection.call(this, details);
        } catch (Exception e) {
            loadFailedCallback(details);
        }
    }

    void loadSuccessCallback(Object data, SLTCallBackProperties properties) {
        if (data != null) {
            cacheLevelContentData(properties.getPack(), properties.getLevel(), data);
        }
        else {
            data = loadLevelContentDataInternally(properties.getPack(), properties.getLevel());
        }

        if (data != null) {
            contentDataLoadSuccessCallback(properties.getLevel(), gson.fromJson(data.toString(), LevelData.class));
        }
        else {
            contentDataLoadFailedCallback();
        }
    }

    void loadFailedCallback(SLTCallBackProperties properties) {
        Object contentData = loadLevelContentDataInternally(properties.getPack(), properties.getLevel());
        contentDataLoadSuccessCallback(properties.getLevel(), gson.fromJson(contentData.toString(), LevelData.class));
    }

    protected void contentDataLoadSuccessCallback(SLTLevel level, LevelData data) {
        level.updateContent(data);
        saltrHttpDataHandler.onSuccess();
    }

    protected void contentDataLoadFailedCallback() {
        System.out.println("[Saltr] ERROR: Level data is not loaded.");
        saltrHttpDataHandler.onFail();
    }
}
