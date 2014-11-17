/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr.response;

public class SLTResponseTemplate {
    protected Boolean success;
    protected SLTResponseError_ error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public SLTResponseError_ getError() {
        return error;
    }

    public void setError(SLTResponseError_ error) {
        this.error = error;
    }
}
