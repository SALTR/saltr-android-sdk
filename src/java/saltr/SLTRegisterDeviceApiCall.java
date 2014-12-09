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

public class SLTRegisterDeviceApiCall extends SLTApiCall {
    private SLTRegisterDeviceDelegate delegate;
    private String email;
    private String clientKey;
    private String deviceId;
    private boolean devMode;
    private String model;
    private String os;

    public SLTRegisterDeviceApiCall(int timeout, boolean devMode, String email, String clientKey, String deviceId, String model, String os) {
        super(timeout);
        this.email = email;
        this.clientKey = clientKey;
        this.deviceId = deviceId;
        this.devMode = devMode;
        this.model = model;
        this.os = os;
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

    public void call(SLTRegisterDeviceDelegate delegate) {
        this.delegate = delegate;
        SLTHttpsConnection connection = createRegisterDevice();
        call(connection);
    }

    private SLTHttpsConnection createRegisterDevice() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("apiVersion", SLTSaltr.API_VERSION);
        args.put("devMode", devMode);
        args.put("type", SLTConfig.DEVICE_TYPE_ANDROID);
        args.put("platform", SLTConfig.DEVICE_PLATFORM_ANDROID);
        args.put("action", SLTConfig.ACTION_DEV_REGISTER_DEVICE);
        args.put("clientKey", clientKey);
        args.put("source", model);
        args.put("os", os);

        if (deviceId != null) {
            args.put("id", deviceId);
        }
        else {
            throw new SLTRuntimeException("Field 'deviceId' is a required.");
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
