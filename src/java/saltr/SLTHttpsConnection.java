/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import android.os.AsyncTask;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SLTHttpsConnection extends AsyncTask<SLTApiCall, Void, String> {

    private List<NameValuePair> params;
    private URL url;

    private Map<String, Object> callbackParams;

    SLTHttpsConnection(Map<String, Object> callParams) {
        params = new ArrayList<NameValuePair>();
        this.callbackParams = callParams;
    }

    @Override
    protected String doInBackground(SLTApiCall... arg) {
        SLTApiCall api = arg[0];
        StringBuilder builder = new StringBuilder();
        String response = null;

        try {
            URLConnection connection = url.openConnection();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            response = builder.toString();

            api.onSuccess(response, callbackParams);
        } catch (IOException e) {
            api.onFailure(callbackParams);
            e.printStackTrace();
        }
        return response;
    }

    void setParameters(String key, Object value) {
        params.add(new BasicNameValuePair(key, value.toString()));
    }

    void setUrl(String urlStr) throws MalformedURLException {
        String paramString = URLEncodedUtils.format(params, "utf-8");
        urlStr += "?" + paramString;
        url = new URL(urlStr);
    }
}
