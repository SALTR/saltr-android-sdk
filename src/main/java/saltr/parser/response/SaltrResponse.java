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

    public SaltrResponse(String status, T responseData) {
        this.status = status;
        this.responseData = responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getResponseData() {
        return responseData;
    }
}
