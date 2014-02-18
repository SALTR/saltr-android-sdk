/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.resource;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.Response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpConnection {
    private String urlParameters;
    private String targetURL;

    public HttpConnection(String targetURL, Map<String, String> urlParameters) {
        this.targetURL = targetURL;
        this.urlParameters = convertUrlParameters(urlParameters);
    }

    public void setUrlParameters(Map<String, String> urlParameters) {
        this.urlParameters = convertUrlParameters(urlParameters);
    }

    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }

    public String excutePost() {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("charset", "utf-8");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    public void call(String url, FluentStringsMap params) throws IOException {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.preparePost(url).setQueryParameters(params).execute(new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                System.out.println("success");
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                System.out.println("failure");
            }
        });

    }

    private String convertUrlParameters(Map<String, String> paramsMap) {
        String params = "";
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
//            params += entry.getKey().replaceAll("\\\\", "") + "=" + entry.getValue().replaceAll("\\\\", "") + "&";
            params += entry.getKey() + "=" + entry.getValue() + "&";
        }
        return params;
    }
}
