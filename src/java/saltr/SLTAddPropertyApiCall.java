/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.util.Log;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class SLTAddPropertyApiCall extends SLTApiCall {
    private String clientKey;
    private String socialId;
    private Object basicProperties;
    private Object customProperties;

    public SLTAddPropertyApiCall(String clientKey, String socialId, Object basicProperties, Object customProperties) {
        this.clientKey = clientKey;
        this.socialId = socialId;
        this.basicProperties = basicProperties;
        this.customProperties = customProperties;
    }

    @Override
    public void onConnectionSuccess(String response) {

    }

    @Override
    public void onConnectionFailure() {

    }

    public void call() {
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
        args.put("action", SLTConfig.ACTION_ADD_PROPERTIES);

        if (socialId != null) {
            args.put("socialId", socialId);
        }

        if (basicProperties != null) {
            args.put("basicProperties", basicProperties);
        }

        if (customProperties != null) {
            args.put("customProperties", customProperties);
        }

        SLTHttpsConnection connection = new SLTHttpsConnection();
        connection.setParameters("args", gson.toJson(args));

        try {
            connection.setUrl(SLTConfig.SALTR_API_URL);
        } catch (MalformedURLException e) {
            Log.e("SALTR", e.getMessage());
        }

        return connection;
    }
}
