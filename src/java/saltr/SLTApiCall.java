package saltr;/*
 * Copyright (c) 2014 Plexonic Ltd
 */

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import saltr.game.SLTLevel;
import saltr.response.SLTResponse;
import saltr.response.SLTResponseAppData;
import saltr.response.level.SLTResponseLevelContentData;

import java.net.MalformedURLException;
import java.util.*;

public class SLTApiCall {

    private SLTIAppDataDelegate appDataDelegate;
    private SLTILevelContentDelegate levelContentDelegate;
    private final Gson gson;


    public SLTApiCall() {
        gson = new Gson();
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
        connection.setParameters("action", SLTConfig.CMD_ADD_PROPERTIES);

        try {
            connection.setUrl(SLTConfig.SALTR_API_URL);
        } catch (MalformedURLException e) {
            Log.e("SALTR", e.getMessage());
        }

        return connection;
    }

    public void syncDeveloperFeatures(String clientKey, String socialId, Map<String, SLTFeature> developerFeatures) {
        SLTHttpsConnection connection = createSyncFeaturesConnection(clientKey, socialId, developerFeatures);
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

    private SLTHttpsConnection createSyncFeaturesConnection(String clientKey, String socialId, Map<String, SLTFeature> developerFeatures) {
        List<Map<String, Object>> featureList = new ArrayList<Map<String, Object>>();
        for (Map.Entry<String, SLTFeature> entry : developerFeatures.entrySet()) {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            tempMap.put("token", entry.getValue().getToken());
            tempMap.put("value", gson.toJson(entry.getValue().getProperties()));
            featureList.add(tempMap);
        }

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("clientKey", clientKey);
        args.put("developerFeatures", featureList);

        if (socialId != null) {
            args.put("socialId", socialId);
        }

        Map<String, Object> callbackParams = new HashMap<String, Object>();
        callbackParams.put("dataType", SLTDataType.FEATURE);
        SLTHttpsConnection connection = new SLTHttpsConnection(callbackParams);


        connection.setParameters("args", gson.toJson(args));
        connection.setParameters("action", SLTConfig.CMD_DEV_SYNC_FEATURES);

        try {
            connection.setUrl(SLTConfig.SALTR_DEVAPI_URL);
        } catch (MalformedURLException e) {
            Log.e("SALTR", e.getMessage());
        }

        return connection;
    }

    private SLTHttpsConnection createAppDataConnection(String clientKey, String deviceId, String socialId, Object basicProperties, Object customProperties) {
        Map<String, Object> args = new HashMap<String, Object>();

        args.put("clientKey", clientKey);
        args.put("deviceId", deviceId);

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
        connection.setParameters("action", SLTConfig.CMD_APP_DATA);

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
            SLTResponse<SLTResponseAppData> data = gson.fromJson(response, new TypeToken<SLTResponse<SLTResponseAppData>>() {
            }.getType());

            if (data == null || data.getStatus().equals(SLTConfig.RESULT_ERROR)) {
                appDataDelegate.appDataLoadFailCallback();
            }
            appDataDelegate.appDataLoadSuccessCallback(data);
        }
        else if (dataType.equals(SLTDataType.LEVEL)) {
            SLTResponseLevelContentData data = gson.fromJson(response.toString(), SLTResponseLevelContentData.class);
            SLTLevel sltLevel = (SLTLevel) callbackParams.get("level");

            if (data == null) {
                levelContentDelegate.loadFromSaltrFailCallback(sltLevel);
            }

            levelContentDelegate.loadFromSaltrSuccessCallback(data, sltLevel);
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
}
