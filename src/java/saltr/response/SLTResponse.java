/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response;

public class SLTResponse<T> {
    protected String status;
    protected T responseData;
    protected String responseMessage;
    protected int errorCode;

    public SLTResponse(String status, T responseData) {
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
