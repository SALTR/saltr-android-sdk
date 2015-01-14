/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.status;

import android.util.Log;

/**
 * The SLTStatus class represents the status information of an operation performed by SDK.
 */
public class SLTStatus {
    /**
     * Specifies the authorization error.
     */
    public static final int AUTHORIZATION_ERROR = 1001;

    /**
     * Specifies the validation error.
     */
    public static final int VALIDATION_ERROR = 1002;

    /**
     * Specifies the API error.
     */
    public static final int API_ERROR = 1003;

    /**
     * Specifies the parse error.
     */
    public static final int PARSE_ERROR = 1004;

    /**
     * Specifies the registration required error.
     */
    public static final int REGISTRATION_REQUIRED_ERROR_CODE = 2001;

    /**
     * Specifies the client error.
     */
    public static final int CLIENT_ERROR_CODE = 2002;

    /**
     * Specifies the client app data load fail.
     */
    public static final int CLIENT_APP_DATA_LOAD_FAIL = 2040;

    /**
     * Specifies the client level content load fail.
     */
    public static final int CLIENT_LEVEL_CONTENT_LOAD_FAIL = 2041;

    /**
     * Specifies the client app data concurrent load refused.
     */
    public static final int CLIENT_APP_DATA_CONCURRENT_LOAD_REFUSED = 2042;

    /**
     * Specifies the client features parse error.
     */
    public static final int CLIENT_FEATURES_PARSE_ERROR = 2050;

    /**
     * Specifies the client experiments parse error.
     */
    public static final int CLIENT_EXPERIMENTS_PARSE_ERROR = 2051;

    /**
     * Specifies the client levels parse error.
     */
    public static final int CLIENT_LEVELS_PARSE_ERROR = 2052;

    private int statusCode;
    private String statusMessage;

    /**
     * Class constructor.
     *
     * @param statusCode    The status code.
     * @param statusMessage The status message.
     */
    public SLTStatus(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        Log.e("SALTR", statusMessage);
    }

    /**
     * @return The status message.
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @return The status code.
     */
    public int getStatusCode() {
        return statusCode;
    }
}
