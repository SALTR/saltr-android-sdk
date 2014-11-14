package saltr;/*
 * Copyright (c) 2014 Plexonic Ltd
 */

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import saltr.exception.SLTRuntimeException;
import saltr.game.SLTLevel;
import saltr.response.SLTResponse;
import saltr.response.SLTResponseAppData;
import saltr.response.SLTResponseClientData;
import saltr.response.SLTResponseTemplate;
import saltr.response.level.SLTResponseLevelContentData;

import java.net.MalformedURLException;
import java.util.*;

public class SLTApiCall {

    private SLTIAppDataDelegate appDataDelegate;
    private SLTILevelContentDelegate levelContentDelegate;
    private SLTSyncDelegate syncDelegate;
    private SLTAddDeviceDelegate addDeviceDelegate;
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

    public void sync(SLTSyncDelegate delegate, String clientKey, String socialId, String deviceId, Map<String, SLTFeature> developerFeatures) {
        syncDelegate = delegate;
        SLTHttpsConnection connection = createSyncClientConnection(clientKey, socialId, deviceId, developerFeatures);
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

    public void addDeviceToSaltr(String deviceName, String email, String clientKey, String deviceId, SLTAddDeviceDelegate delegate) {
        addDeviceDelegate = delegate;
        SLTHttpsConnection connection = createDeviceToSaltr(deviceName, email, clientKey, deviceId);
        connection.execute(this);
    }

    private SLTHttpsConnection createSyncClientConnection(String clientKey, String socialId, String deviceId, Map<String, SLTFeature> developerFeatures) {
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
        callbackParams.put("dataType", SLTDataType.CLIENT_DATA);
        SLTHttpsConnection connection = new SLTHttpsConnection(callbackParams);

        connection.setParameters("args", gson.toJson(args));
        connection.setParameters("action", SLTConfig.ACTION_DEV_SYNC_CLIENT_DATA);

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

    private SLTHttpsConnection createDeviceToSaltr(String deviceName, String email, String clientKey, String deviceId) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("apiVersion", SLTSaltr.API_VERSION);
        args.put("devMode", devMode);
        args.put("type", SLTConfig.DEVICE_TYPE_ANDROID);
        args.put("platform", SLTConfig.DEVICE_PLATFORM_ANDROID);

        if (deviceId != null) {
            args.put("id", deviceId);
        }
        else {
            throw new SLTRuntimeException("Field 'deviceId' is a required.");
        }

        if (deviceName != null) {
            args.put("name", deviceName);
        }
        else {
            throw new SLTRuntimeException("Field 'name' is a required.");
        }

        if (email != null) {
            args.put("email", email);
        }
        else {
            throw new SLTRuntimeException("Field 'email' is a required.");
        }

        Map<String, Object> callbackParams = new HashMap<String, Object>();
        callbackParams.put("dataType", SLTDataType.ADD_DEVICE);
        SLTHttpsConnection connection = new SLTHttpsConnection(callbackParams);

        connection.setParameters("args", gson.toJson(args));
        connection.setParameters("action", SLTConfig.ACTION_DEV_REGISTER_IDENTITY);
        connection.setParameters("clientKey", clientKey);

        try {
            connection.setUrl(SLTConfig.SALTR_DEVAPI_URL);
        } catch (MalformedURLException e) {
            Log.e("SALTR", e.getMessage());
        }

        return connection;
    }

    public void onSuccess(String response, Map<String, Object> callbackParams) {
        SLTDataType dataType = (SLTDataType) callbackParams.get("dataType");

        if (dataType.equals(SLTDataType.APP)) {
            try {
                SLTResponse<SLTResponseAppData> data = gson.fromJson(response, new TypeToken<SLTResponse<SLTResponseAppData>>() {
                }.getType());
                if (data != null && data.getResponse() != null && !data.getResponse().isEmpty()) {
                    appDataDelegate.onSuccess(data.getResponse().get(0));
                }
                else {
                    appDataDelegate.onFailure();
                }
            } catch (Exception e) {
                Log.e("SALTR", "Couldn't parse application data sent from server");
                appDataDelegate.onFailure();
            }

        }
        else if (dataType.equals(SLTDataType.LEVEL)) {
            SLTLevel sltLevel = (SLTLevel) callbackParams.get("level");
            try {
                SLTResponseLevelContentData data = gson.fromJson(response, SLTResponseLevelContentData.class);
                if (data == null) {
                    levelContentDelegate.onFailure(sltLevel);
                }
                else {
                    levelContentDelegate.onSuccess(data, sltLevel);
                }
            } catch (Exception e) {
                Log.e("SALTR", "Couldn't parse level data sent from server");
                levelContentDelegate.onFailure(sltLevel);
            }
        }
        else if (dataType.equals(SLTDataType.CLIENT_DATA)) {
            SLTResponse<SLTResponseClientData> data = gson.fromJson(response, new TypeToken<SLTResponse<SLTResponseClientData>>() {
            }.getType());
            if (data == null) {
                Log.e("SALTR", "Incorrect data sent from server.");
            }
            else {
                syncDelegate.onSuccess(data.getResponse().get(0));
            }
        }
        else if (dataType.equals(SLTDataType.ADD_DEVICE)) {
            SLTResponse<SLTResponseTemplate> data = gson.fromJson(response, new TypeToken<SLTResponse<SLTResponseTemplate>>() {
            }.getType());
            if (data == null) {
                Log.e("SALTR", "Incorrect data sent from server.");
            }
            else {
                addDeviceDelegate.onSuccess(data.getResponse().get(0));
            }
        }
    }

    public void onFailure(Map<String, Object> callbackParams) {
        SLTDataType dataType = (SLTDataType) callbackParams.get("dataType");
        if (dataType.equals(SLTDataType.APP)) {
            appDataDelegate.onFailure();
        }
        else if (dataType.equals(SLTDataType.LEVEL)) {
            SLTLevel sltLevel = (SLTLevel) callbackParams.get("level");
            levelContentDelegate.onFailure(sltLevel);
        }
    }
}
