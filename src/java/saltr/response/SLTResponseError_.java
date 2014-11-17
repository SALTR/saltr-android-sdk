/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response;

public class SLTResponseError_ {
    private Integer code;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
