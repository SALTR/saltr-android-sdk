package saltr;/*
 * Copyright (c) 2014 Plexonic Ltd
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import saltr.response.SLTResponse;
import saltr.response.SLTResponseAppData;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ApiCall {

    private SLTIAppDataDelegate appDataDelegate;
    private SLTILevelContentDelegate levelContentDelegate;
    private final Gson gson;


    public ApiCall() {
        gson = new Gson();
    }

    public void loadLevelContent() {
    }

    public void setProperties() {

    }
    public void loadAppData(String clientKey, String deviceId, String socialId, String saltrUserId, Object basicProperties, Object customProperties, SLTIAppDataDelegate delegate) throws Exception{
        appDataDelegate = delegate;
        SLTHttpsConnection connection = createAppDataConnection(clientKey, deviceId, socialId, saltrUserId, basicProperties, customProperties);

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("api", this);
        params.put("params", )
        params.put("dataType", SLTDataType.APP);
        connection.execute(params);
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

        SLTHttpsConnection connection = new SLTHttpsConnection();


        connection.setParameters("args", gson.toJson(args));
        connection.setParameters("cmd", SLTConfig.CMD_APP_DATA);
        connection.setParameters("action", SLTConfig.CMD_APP_DATA);

        connection.setUrl(SLTConfig.SALTR_API_URL);

        return connection;
    }

    public void onSuccess(String response, Object... arg) {
        SLTDataType dataType = (SLTDataType)arg[1];//(SLTDataType)params.get("dataType");
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

        }
    }

    public void onFailure() {
        appDataDelegate.appDataLoadFailCallback();
    }
}
