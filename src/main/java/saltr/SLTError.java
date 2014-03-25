/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

public class SLTError {
    public static final int AUTHORIZATION_ERROR_CODE = 1001;
    public static final int VALIDATION_ERROR_CODE = 1002;
    public static final int API_ERROR_CODE = 1003;
    public static final int GENERAL_ERROR_CODE = 2001;
    public static final int CLIENT_ERROR_CODE = 2002;

    private int errorCode;
    private String errorMessage;

    public SLTError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
