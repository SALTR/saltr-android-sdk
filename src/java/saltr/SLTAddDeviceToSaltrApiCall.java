/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.util.Log;
import com.google.gson.reflect.TypeToken;
import saltr.exception.SLTRuntimeException;
import saltr.response.SLTResponse;
import saltr.response.SLTResponseTemplate;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class SLTAddDeviceToSaltrApiCall extends SLTApiCall {
    private SLTAddDeviceDelegate delegate;
    private String deviceName;
    private String email;
    private String clientKey;
    private String deviceId;
    private boolean devMode;

    public SLTAddDeviceToSaltrApiCall(int timeout, boolean devMode, String deviceName, String email, String clientKey, String deviceId) {
        super(timeout);
        this.deviceName = deviceName;
        this.email = email;
        this.clientKey = clientKey;
        this.deviceId = deviceId;
        this.devMode = devMode;
    }

    @Override
    public void onConnectionSuccess(String response) {
        SLTResponse<SLTResponseTemplate> data = gson.fromJson(response, new TypeToken<SLTResponse<SLTResponseTemplate>>() {
        }.getType());
        if (data == null) {
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

    public void call(SLTAddDeviceDelegate delegate) {
        this.delegate = delegate;
        SLTHttpsConnection connection = createDeviceToSaltr(deviceName, email, clientKey, deviceId);
        call(connection);
    }

    private SLTHttpsConnection createDeviceToSaltr(String deviceName, String email, String clientKey, String deviceId) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("apiVersion", SLTSaltr.API_VERSION);
        args.put("devMode", devMode);
        args.put("type", SLTConfig.DEVICE_TYPE_ANDROID);
        args.put("platform", SLTConfig.DEVICE_PLATFORM_ANDROID);
        args.put("action", SLTConfig.ACTION_DEV_REGISTER_IDENTITY);
        args.put("clientKey", clientKey);

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
