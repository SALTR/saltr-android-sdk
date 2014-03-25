/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.response;

public class SaltrResponse<T> {
    protected String status;
    protected T responseData;
    protected String responseMessage;
    protected int errorCode;

    public SaltrResponse(String status, T responseData) {
        this.status = status;
        this.responseData = responseData;
    }

    public String getStatus() {

        return status;
    }

    public T getResponseData() {
        return responseData;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
