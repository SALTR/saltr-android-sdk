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

//TODO:: @daal Why this class has saltr property?
class SLTHttpsConnection extends AsyncTask<SLTSaltr, Void, String> {

    private SLTIDataHandler dataHandler;
    private List<NameValuePair> params;
    private URL url;
    private SLTCallBackProperties properties;

    SLTHttpsConnection(SLTIDataHandler dataHandler, SLTCallBackProperties properties) {
        params = new ArrayList<NameValuePair>();
        this.dataHandler = dataHandler;
        this.properties = properties;
    }

    @Override
    protected String doInBackground(SLTSaltr... arg) {
        SLTSaltr saltr = arg[0];
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

            onSuccess(saltr, response);
        } catch (IOException e) {
            onFailure(saltr);
            e.printStackTrace();
        }
        return response;
    }

    protected void onPostExecute(Long result) {
        System.out.println("Downloaded " + result + " bytes");
    }

    void setParameters(String key, Object value) {
        params.add(new BasicNameValuePair(key, value.toString()));
    }

    void setUrl(String urlStr) throws MalformedURLException {
        String paramString = URLEncodedUtils.format(params, "utf-8");
        urlStr += "?" + paramString;
        url = new URL(urlStr);
    }


    //TODO @daal. What is this? Why is this.dataHandler for?
    private void onSuccess(SLTSaltr saltr, String responseData) {
        try {
            if (properties.getDataType().getValue().equals(SLTDataType.APP.getValue())) {
                saltr.appDataLoadSuccessCallback(responseData);
            }
            else if (properties.getDataType().getValue().equals(SLTDataType.LEVEL.getValue())) {
                saltr.loadFromSaltrSuccessCallback(responseData, properties.getLevel());
            }
            else if (properties.getDataType().getValue().equals(SLTDataType.PLAYER_PROPERTY.getValue())) {
                System.out.println("success");
            }
            else if (properties.getDataType().getValue().equals(SLTDataType.FEATURE.getValue())) {
                System.out.println("[Saltr] Dev feature Sync is complete.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onFailure(SLTSaltr saltr) {
        if (properties.getDataType().getValue().equals(SLTDataType.APP.getValue())) {
            saltr.appDataLoadFailCallback();
        }
        else if (properties.getDataType().getValue().equals(SLTDataType.LEVEL.getValue())) {
            try {
                saltr.loadFromSaltrFailCallback(properties.getLevel());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (properties.getDataType().getValue().equals(SLTDataType.PLAYER_PROPERTY.getValue())) {
            System.err.println("error");
        }
        else if (properties.getDataType().getValue().equals(SLTDataType.FEATURE.getValue())) {
            System.err.println("[Saltr] Dev feature Sync has failed.");
        }
    }
}
