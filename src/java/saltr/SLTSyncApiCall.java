/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.util.Log;
import com.google.gson.reflect.TypeToken;
import saltr.exception.SLTRuntimeException;
import saltr.response.SLTResponse;
import saltr.response.SLTResponseClientData;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SLTSyncApiCall extends SLTApiCall {
    private boolean devMode;
    private String clientKey;
    private String socialId;
    private String deviceId;
    private Map<String, SLTFeature> developerFeatures;
    private SLTSyncDelegate delegate;

    public SLTSyncApiCall(int timeout, boolean devMode, String clientKey, String socialId, String deviceId, Map<String, SLTFeature> developerFeatures) {
        super(timeout);
        this.devMode = devMode;
        this.clientKey = clientKey;
        this.socialId = socialId;
        this.deviceId = deviceId;
        this.developerFeatures = developerFeatures;
    }

    @Override
    public void onConnectionSuccess(String response) {
        SLTResponse<SLTResponseClientData> data = gson.fromJson(response, new TypeToken<SLTResponse<SLTResponseClientData>>() {
        }.getType());
        if (data == null || data.getFirst() == null || data.getFirst().getSuccess() == null) {
            Log.e("SALTR", "Incorrect data sent from server.");
            delegate.onFailure();
        }
        else {
            delegate.onSuccess(data.getFirst());
        }
    }

    @Override
    public void onConnectionFailure() {
        delegate.onFailure();
    }

    public void call(SLTSyncDelegate delegate) {
        this.delegate = delegate;
        SLTHttpsConnection connection = createSyncClientConnection(clientKey, socialId, deviceId, developerFeatures);
        call(connection);
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
        args.put("action", SLTConfig.ACTION_DEV_SYNC_CLIENT_DATA);

        if (deviceId != null) {
            args.put("deviceId", deviceId);
        }
        else {
            throw new SLTRuntimeException("Field 'deviceId' is a required.");
        }

        if (socialId != null) {
            args.put("socialId", socialId);
        }

        SLTHttpsConnection connection = new SLTHttpsConnection();
        connection.setParameters("args", gson.toJson(args));

        try {
            connection.setUrl(SLTConfig.SALTR_DEVAPI_URL);
        } catch (MalformedURLException e) {
            Log.e("SALTR", e.getMessage());
        }

        return connection;
    }
}
