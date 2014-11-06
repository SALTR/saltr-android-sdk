package saltr;/*
 * Copyright (c) 2014 Plexonic Ltd
 */

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import saltr.exception.SLTRuntimeException;
import saltr.game.SLTLevel;
import saltr.response.*;
import saltr.response.level.SLTResponseLevelContentData;

import java.net.MalformedURLException;
import java.util.*;

public class SLTApiCall {

    private SLTIAppDataDelegate appDataDelegate;
    private SLTILevelContentDelegate levelContentDelegate;
    private final Gson gson;
    private boolean devMode;


    public SLTApiCall(boolean devMode) {
        gson = new Gson();
        this.devMode = devMode;
    }

    public void addProperties(String clientKey, String socialId, Object basicProperties, Object customProperties) {
        if (basicProperties == null && customProperties == null) {
            return;
        }
        SLTHttpsConnection connection = createPlayerPropertiesConnection(clientKey, socialId, basicProperties, customProperties);
        connection.execute(this);
    }

    private SLTHttpsConnection createPlayerPropertiesConnection(String clientKey, String socialId, Object basicProperties, Object customProperties) {
        Map<String, Object> args = new HashMap<>();

        args.put("clientKey", clientKey);
        args.put("client", SLTSaltr.CLIENT);

        if (socialId != null) {
            args.put("socialId", socialId);
        }

        if (basicProperties != null) {
            args.put("basicProperties", basicProperties);
        }

        if (customProperties != null) {
            args.put("customProperties", customProperties);
        }

        Map<String, Object> callbackParams = new HashMap<String, Object>();
        callbackParams.put("dataType", SLTDataType.PLAYER_PROPERTY);
        SLTHttpsConnection connection = new SLTHttpsConnection(callbackParams);

        connection.setParameters("args", gson.toJson(args));
        connection.setParameters("action", SLTConfig.ACTION_ADD_PROPERTIES);

        try {
            connection.setUrl(SLTConfig.SALTR_API_URL);
        } catch (MalformedURLException e) {
            Log.e("SALTR", e.getMessage());
        }

        return connection;
    }

    public void syncDeveloperFeatures(String clientKey, String socialId, String deviceId, Map<String, SLTFeature> developerFeatures) {
        SLTHttpsConnection connection = createSyncFeaturesConnection(clientKey, socialId, deviceId, developerFeatures);
        connection.execute(this);
    }

    public void loadLevelContent(SLTLevel level, SLTILevelContentDelegate delegate) {
        levelContentDelegate = delegate;
        String dataUrl = level.getContentUrl() + "?_time_=" + new Date().getTime();
        Map<String, Object> callbackParams = new HashMap<String, Object>();
        callbackParams.put("dataType", SLTDataType.LEVEL);
        callbackParams.put("level", level);
        SLTHttpsConnection connection = new SLTHttpsConnection(callbackParams);
        try {
            connection.setUrl(dataUrl);
            connection.execute(this);
        } catch (MalformedURLException e) {
            onFailure(callbackParams);
        }

    }

    public void loadAppData(String clientKey, String deviceId, String socialId, Object basicProperties, Object customProperties, SLTIAppDataDelegate delegate) {
        appDataDelegate = delegate;
        SLTHttpsConnection connection = createAppDataConnection(clientKey, deviceId, socialId, basicProperties, customProperties);
        connection.execute(this);
    }

    private SLTHttpsConnection createSyncFeaturesConnection(String clientKey, String socialId, String deviceId, Map<String, SLTFeature> developerFeatures) {
        List<Map<String, Object>> featureList = new ArrayList<Map<String, Object>>();
        for (Map.Entry<String, SLTFeature> entry : developerFeatures.entrySet()) {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            tempMap.put("token", entry.getValue().getToken());
            tempMap.put("value", gson.toJson(entry.getValue().getProperties()));
            featureList.add(tempMap);
        }

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("apiVersion", SLTSaltr.API_VERSION);
        args.put("clientKey", clientKey);
        args.put("client", SLTSaltr.CLIENT);
        args.put("devMode", devMode);
        args.put("developerFeatures", featureList);

        if (deviceId != null) {
            args.put("deviceId", deviceId);
        }
        else {
            throw new SLTRuntimeException("Field 'deviceId' is a required.");
        }

        if (socialId != null) {
            args.put("socialId", socialId);
        }

        Map<String, Object> callbackParams = new HashMap<String, Object>();
        callbackParams.put("dataType", SLTDataType.FEATURE);
        SLTHttpsConnection connection = new SLTHttpsConnection(callbackParams);

        connection.setParameters("args", gson.toJson(args));
        connection.setParameters("action", SLTConfig.ACTION_DEV_SYNC_FEATURES);

        try {
            connection.setUrl(SLTConfig.SALTR_DEVAPI_URL);
        } catch (MalformedURLException e) {
            Log.e("SALTR", e.getMessage());
        }

        return connection;
    }

    private SLTHttpsConnection createAppDataConnection(String clientKey, String deviceId, String socialId, Object basicProperties, Object customProperties) {
        Map<String, Object> args = new HashMap<String, Object>();

        args.put("apiVersion", SLTSaltr.API_VERSION);
        args.put("clientKey", clientKey);
        args.put("client", SLTSaltr.CLIENT);

        if (deviceId != null) {
            args.put("deviceId", deviceId);
        }
        else {
            throw new SLTRuntimeException("Field 'deviceId' is a required.");
        }

        args.put("devMode", devMode);

        if (socialId != null) {
            args.put("socialId", socialId);
        }

        if (basicProperties != null) {
            args.put("basicProperties", basicProperties);
        }

        if (customProperties != null) {
            args.put("customProperties", customProperties);
        }

        Map<String, Object> callbackParams = new HashMap<String, Object>();
        callbackParams.put("dataType", SLTDataType.APP);
        SLTHttpsConnection connection = new SLTHttpsConnection(callbackParams);


        connection.setParameters("args", gson.toJson(args));
        connection.setParameters("action", SLTConfig.ACTION_GET_APP_DATA);

        try {
            connection.setUrl(SLTConfig.SALTR_API_URL);
        } catch (MalformedURLException e) {
            Log.e("SALTR", e.getMessage());
            onFailure(callbackParams);
        }

        return connection;
    }

    public void onSuccess(String response, Map<String, Object> callbackParams) {
        SLTDataType dataType = (SLTDataType) callbackParams.get("dataType");

        if (dataType.equals(SLTDataType.APP)) {
            try {
                SLTResponse data = gson.fromJson(response, SLTResponse.class);

                if (data != null) {
                    SLTResponseAppData parsedData = parseAppData(data);
                    appDataDelegate.appDataLoadSuccessCallback(parsedData);
                }
                else {
                    appDataDelegate.appDataLoadFailCallback();
                }
            } catch (Exception e) {
                Log.e("SALTR", "Couldn't parse application data sent from server");
                appDataDelegate.appDataLoadFailCallback();
            }

        }
        else if (dataType.equals(SLTDataType.LEVEL)) {
            SLTLevel sltLevel = (SLTLevel) callbackParams.get("level");
            try {
                SLTResponseLevelContentData data = gson.fromJson(response, SLTResponseLevelContentData.class);

                if (data == null) {
                    levelContentDelegate.loadFromSaltrFailCallback(sltLevel);
                }

                levelContentDelegate.loadFromSaltrSuccessCallback(data, sltLevel);
            } catch (Exception e) {
                Log.e("SALTR", "Couldn't parse level data sent from server");
                levelContentDelegate.loadFromSaltrFailCallback(sltLevel);
            }
        }
        else if (dataType.equals(SLTDataType.FEATURE)) {
            SLTResponse data = gson.fromJson(response, SLTResponse.class);

            if (data == null) {
                Log.e("SALTR", "Dev feature Sync's response.jsonData is null.");
            }

        }
    }

    public void onFailure(Map<String, Object> callbackParams) {
        SLTDataType dataType = (SLTDataType) callbackParams.get("dataType");
        if (dataType.equals(SLTDataType.APP)) {
            appDataDelegate.appDataLoadFailCallback();
        }
        else if (dataType.equals(SLTDataType.LEVEL)) {
            SLTLevel sltLevel = (SLTLevel) callbackParams.get("level");
            levelContentDelegate.loadFromSaltrFailCallback(sltLevel);
        }
    }

    private SLTResponseAppData parseAppData(SLTResponse data) {
        SLTResponseAppData appData = new SLTResponseAppData();

        for (Map<String, String> map : data.getResponse()) {
            String key = map.entrySet().iterator().next().getKey();
            String value = map.entrySet().iterator().next().getValue();
            switch (key) {
                case "success":
                    appData.setSuccess(Boolean.valueOf(value));
                    break;
                case "levelType":
                    appData.setLevelType(value);
                    break;
                case "levelPacks":
                    List<SLTResponsePack> packs = gson.fromJson(value, new TypeToken<List<SLTResponsePack>>() {
                    }.getType());
                    appData.setLevelPacks(packs);
                    break;
                case "features":
                    List<SLTResponseFeature> features = gson.fromJson(value, new TypeToken<List<SLTResponseFeature>>() {
                    }.getType());
                    appData.setFeatures(features);
                    break;
                case "experiments":
                    List<SLTResponseExperiment> experiments = gson.fromJson(value, new TypeToken<List<SLTResponseExperiment>>() {
                    }.getType());
                    appData.setExperiments(experiments);
                    break;
                case "error":
                    SLTResponseError error = gson.fromJson(value, SLTResponseError.class);
                    appData.setError(error);
                    break;
            }
        }
        return appData;
    }
}
