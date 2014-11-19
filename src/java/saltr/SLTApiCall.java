/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import com.google.gson.Gson;

public abstract class SLTApiCall {

    protected Gson gson;

    public SLTApiCall() {
        gson = new Gson();
    }

    public abstract void onConnectionSuccess(String response);

    public abstract void onConnectionFailure();
}
