/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.util.Log;
import com.google.gson.reflect.TypeToken;
import saltr.exception.SLTRuntimeException;
import saltr.game.SLTLevel;
import saltr.game.SLTLevelPack;
import saltr.response.SLTResponse;
import saltr.response.SLTResponseAppData;
import saltr.status.SLTStatus;
import saltr.status.SLTStatusExperimentsParseError;
import saltr.status.SLTStatusFeaturesParseError;
import saltr.status.SLTStatusLevelsParseError;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SLTAppDataApiCall extends SLTApiCall {
    private SLTIAppDataDelegate delegate;
    private boolean devMode;
    private boolean useNoLevels;
    private String clientKey;
    private Object customProperties;
    private String deviceId;
    private String socialId;
    private Object basicProperties;

    public SLTAppDataApiCall(int timeout, boolean devMode, boolean useNoLevels, String clientKey, String deviceId, String socialId, Object basicProperties, Object customProperties) {
        super(timeout);
        this.devMode = devMode;
        this.useNoLevels = useNoLevels;
        this.clientKey = clientKey;
        this.deviceId = deviceId;
        this.socialId = socialId;
        this.basicProperties = basicProperties;
        this.customProperties = customProperties;
    }

    public void call(SLTIAppDataDelegate delegate) {
        this.delegate = delegate;
        SLTHttpsConnection connection = createAppDataConnection(clientKey, deviceId, socialId, basicProperties, customProperties);
        call(connection);
    }


    private SLTHttpsConnection createAppDataConnection(String clientKey, String deviceId, String socialId, Object basicProperties, Object customProperties) {
        Map<String, Object> args = new HashMap<String, Object>();

        args.put("apiVersion", SLTSaltr.API_VERSION);
        args.put("clientKey", clientKey);
        args.put("client", SLTSaltr.CLIENT);
        args.put("action", SLTConfig.ACTION_GET_APP_DATA);

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

        SLTHttpsConnection connection = new SLTHttpsConnection();
        connection.setParameters("args", gson.toJson(args));

        try {
            connection.setUrl(SLTConfig.SALTR_API_URL);
        } catch (MalformedURLException e) {
            Log.e("SALTR", e.getMessage());
            delegate.onFailure(null);
        }

        return connection;
    }

    private Boolean isResponseValid(SLTResponse<SLTResponseAppData> data) {
        return data != null && data.getResponse() != null && !data.getResponse().isEmpty();
    }

    public void onConnectionSuccess(String response) {
        SLTResponse<SLTResponseAppData> data = gson.fromJson(response, new TypeToken<SLTResponse<SLTResponseAppData>>() {
        }.getType());

        SLTResponseAppData appDataResponse = data.getFirst();

        if (isResponseValid(data)) {
            if (appDataResponse.getSuccess()) {
                Map<String, SLTFeature> features;
                List<SLTExperiment> experiments;
                List<SLTLevelPack> levelPacks = null;

                try {
                    features = SLTDeserializer.decodeFeatures(appDataResponse);
                } catch (Exception e) {
                    delegate.onFailure(new SLTStatusFeaturesParseError());
                    return;
                }

                try {
                    experiments = SLTDeserializer.decodeExperiments(appDataResponse);
                } catch (Exception e) {
                    delegate.onFailure(new SLTStatusExperimentsParseError());
                    return;
                }

                if (!useNoLevels && !SLTLevel.LEVEL_TYPE_NONE.equals(appDataResponse.getLevelType())) {
                    try {
                        levelPacks = SLTDeserializer.decodeLevels(appDataResponse);
                    } catch (Exception e) {
                        delegate.onFailure(new SLTStatusLevelsParseError());
                        return;
                    }
                }

                delegate.onSuccess(appDataResponse, features, experiments, levelPacks);
            }
            else {
                delegate.onFailure(new SLTStatus(appDataResponse.getError().getCode(), appDataResponse.getError().getMessage()));
            }
        }
        else {
            delegate.onFailure(null);
        }
    }

    public void onConnectionFailure() {
        delegate.onFailure(null);
    }
}

