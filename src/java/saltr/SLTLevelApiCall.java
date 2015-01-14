/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import android.util.Log;
import saltr.game.SLTLevel;
import saltr.response.level.SLTResponseLevelContentData;

import java.net.MalformedURLException;

class SLTLevelApiCall extends SLTApiCall {
    private SLTILevelContentDelegate delegate;
    private SLTLevel level;

    SLTLevelApiCall(int timeout, SLTLevel level) {
        super(timeout);
        this.level = level;
    }

    void call(SLTILevelContentDelegate delegate) {
        this.delegate = delegate;
        SLTHttpsConnection connection = new SLTHttpsConnection();
        try {
            connection.setUrl(level.getContentUrl());
            call(connection);
        } catch (MalformedURLException e) {
            this.delegate.onFailure(level);
        }
    }

    @Override
    void onConnectionSuccess(String response) {
        try {
            SLTResponseLevelContentData data = gson.fromJson(response, SLTResponseLevelContentData.class);
            if (data == null) {
                delegate.onFailure(level);
            }
            else {
                delegate.onSuccess(data, level);
            }
        } catch (Exception e) {
            Log.e("SALTR", "Couldn't parse level data sent from server");
            delegate.onFailure(level);
        }
    }

    @Override
    void onConnectionFailure() {
        delegate.onFailure(level);
    }
}
