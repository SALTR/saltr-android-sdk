package saltr;/*
 * Copyright (c) 2014 Plexonic Ltd
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import saltr.game.SLTLevel;
import saltr.response.SLTResponse;
import saltr.response.SLTResponseAppData;
import saltr.response.level.SLTResponseLevelContentData;

import java.net.MalformedURLException;
import java.util.*;

public class ApiCall {

    private SLTIAppDataDelegate appDataDelegate;
    private SLTILevelContentDelegate levelContentDelegate;
    private final Gson gson;


    public ApiCall() {
        gson = new Gson();
    }

    public void addProperties(String clientKey, String deviceId, String socialId, String saltrUserId, Object basicProperties, Object customProperties) throws Exception {
        SLTHttpsConnection connection = createPlayerPropertiesConnection(clientKey,deviceId,socialId,saltrUserId,basicProperties,customProperties);
        connection.execute(this);
    }


    private SLTHttpsConnection createPlayerPropertiesConnection(String clientKey, String deviceId, String socialId, String saltrUserId, Object basicProperties, Object customProperties) throws MalformedURLException, Exception  {
        Map<String, Object> args = new HashMap<>();

        args.put("clientKey", clientKey);
        args.put("deviceId", deviceId);

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

        Map<String,Object> callbackParams = new HashMap<String,Object>();
        callbackParams.put("dataType", SLTDataType.PLAYER_PROPERTY);
        SLTHttpsConnection connection = new SLTHttpsConnection(callbackParams);

        connection.setParameters("args", gson.toJson(args));
        connection.setParameters("cmd", SLTConfig.CMD_ADD_PROPERTIES);
        connection.setParameters("action", SLTConfig.CMD_ADD_PROPERTIES);

        connection.setUrl(SLTConfig.SALTR_API_URL);

        return connection;
    }

    public void syncDeveloperFeatures(String clientKey, String deviceId, String socialId, String saltrUserId, Map<String, SLTFeature> developerFeatures) throws Exception {
        SLTHttpsConnection connection = createSyncFeaturesConnection(clientKey, deviceId, socialId, saltrUserId, developerFeatures);
        connection.execute(this);
    }

    public void loadLevelContent(SLTLevel level, SLTILevelContentDelegate delegate) {
        levelContentDelegate = delegate;
        String dataUrl = level.getContentUrl() + "?_time_=" + new Date().getTime();
        Map<String,Object> callbackParams = new HashMap<String,Object>();
        callbackParams.put("dataType", SLTDataType.LEVEL);
        callbackParams.put("level",level);
        SLTHttpsConnection connection = new SLTHttpsConnection(callbackParams);
        try {
            connection.setUrl(dataUrl);
        } catch (MalformedURLException e) {
            //TODO::return fail callback

        }
        connection.execute(this);

    }

    public void loadAppData(String clientKey, String deviceId, String socialId, String saltrUserId, Object basicProperties, Object customProperties, SLTIAppDataDelegate delegate) throws Exception{
        appDataDelegate = delegate;
        SLTHttpsConnection connection = createAppDataConnection(clientKey, deviceId, socialId, saltrUserId, basicProperties, customProperties);

        connection.execute(this);
    }

    private SLTHttpsConnection createSyncFeaturesConnection(String clientKey, String deviceId, String socialId, String saltrUserId, Map<String, SLTFeature> developerFeatures) throws MalformedURLException, Exception {
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
        args.put("deviceId", deviceId);

        if (socialId != null) {
            args.put("socialId", socialId);
        }

        if (saltrUserId != null) {
            args.put("saltrUserId", saltrUserId);
        }

        Map<String,Object> callbackParams = new HashMap<String, Object>();
        callbackParams.put("dataType", SLTDataType.FEATURE);
        SLTHttpsConnection connection = new SLTHttpsConnection(callbackParams);


        connection.setParameters("args", gson.toJson(args));
        connection.setParameters("cmd", SLTConfig.CMD_DEV_SYNC_FEATURES);
        connection.setParameters("action", SLTConfig.CMD_DEV_SYNC_FEATURES);

        connection.setUrl(SLTConfig.SALTR_DEVAPI_URL);

        return connection;
    }



    private SLTHttpsConnection createAppDataConnection(String clientKey, String deviceId, String socialId, String saltrUserId, Object basicProperties, Object customProperties) throws MalformedURLException, Exception {
        Map<String, Object> args = new HashMap<String, Object>();

        args.put("clientKey", clientKey);
        args.put("deviceId", deviceId);

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

        Map<String,Object> callbackParams = new HashMap<String,Object>();
        callbackParams.put("dataType",SLTDataType.APP);
        SLTHttpsConnection connection = new SLTHttpsConnection(callbackParams);


        connection.setParameters("args", gson.toJson(args));
        connection.setParameters("cmd", SLTConfig.CMD_APP_DATA);
        connection.setParameters("action", SLTConfig.CMD_APP_DATA);

        connection.setUrl(SLTConfig.SALTR_API_URL);

        return connection;
    }

    public void onSuccess(String response, Map<String, Object> callbackParams) {
        SLTDataType dataType = (SLTDataType)callbackParams.get("dataType");

        if(dataType.equals(SLTDataType.APP)) {
            SLTResponse<SLTResponseAppData> data = gson.fromJson(response, new TypeToken<SLTResponse<SLTResponseAppData>>() {
            }.getType());

            //TODO::@Daal . @xcho says RESULT_ERROR will not work double check why
            if(data == null || !data.getStatus().equals(SLTConfig.RESULT_SUCCEED)) {
                appDataDelegate.appDataLoadFailCallback();
            }
            appDataDelegate.appDataLoadSuccessCallback(data);
        }
        else if (dataType.equals(SLTDataType.LEVEL)){
            SLTResponseLevelContentData data = gson.fromJson(response.toString(), SLTResponseLevelContentData.class);
            SLTLevel sltLevel = (SLTLevel)callbackParams.get("level");

            if(data == null) {
                levelContentDelegate.loadFromSaltrFailCallback(sltLevel);
            }

            levelContentDelegate.loadFromSaltrSuccessCallback(data, sltLevel);
        }
    }

    public void onFailure(Map<String, Object> callbackParams) {
        SLTDataType dataType = (SLTDataType)callbackParams.get("dataType");
        if(dataType.equals(SLTDataType.APP)) {
            appDataDelegate.appDataLoadFailCallback();
        }
        else if(dataType.equals(SLTDataType.LEVEL)) {
            SLTLevel sltLevel = (SLTLevel)callbackParams.get("level");
            levelContentDelegate.loadFromSaltrFailCallback(sltLevel);
        }
    }
}
