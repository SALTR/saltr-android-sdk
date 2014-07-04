/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.status;

public class SLTStatus {
    public static final int AUTHORIZATION_ERROR = 1001;
    public static final int VALIDATION_ERROR = 1002;
    public static final int API_ERROR = 1003;


    public static final int GENERAL_ERROR_CODE = 2001;
    public static final int CLIENT_ERROR_CODE = 2002;


    public static final int CLIENT_APP_DATA_LOAD_FAIL = 2040;
    public static final int CLIENT_LEVEL_CONTENT_LOAD_FAIL = 2041;

    public static final int CLIENT_FEATURES_PARSE_ERROR = 2050;
    public static final int CLIENT_EXPERIMENTS_PARSE_ERROR = 2051;
    public static final int CLIENT_LEVELS_PARSE_ERROR = 2052;

    private int statusCode;
    private String statusMessage;

    public SLTStatus(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        System.err.println(statusMessage);
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
