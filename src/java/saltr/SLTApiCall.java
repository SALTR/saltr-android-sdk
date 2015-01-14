/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.util.Log;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

abstract class SLTApiCall {

    protected Gson gson;
    protected int timeout;

    protected SLTApiCall(int timeout) {
        this.gson = new Gson();
        this.timeout = timeout;
    }

    protected void call(SLTHttpsConnection connection) {
        if (timeout == 0) {
            try {
                connection.execute(this);
            } catch (Exception e) {
                Log.e("SALTR", "Request has thrown exception");
            }
        }
        else {
            try {
                connection.execute(this).get(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Log.e("SALTR", "Request has interrupted");
                connection.cancel(true);
                onConnectionFailure();
            } catch (ExecutionException e) {
                Log.e("SALTR", "Request has thrown exception");
                connection.cancel(true);
                onConnectionFailure();
            } catch (TimeoutException e) {
                Log.w("SALTR", "Request timed out");
                connection.cancel(true);
                onConnectionFailure();
            }
        }
    }

    abstract void onConnectionSuccess(String response);

    abstract void onConnectionFailure();
}
