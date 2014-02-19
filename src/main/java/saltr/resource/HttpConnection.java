/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.resource;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HttpConnection {
    private String urlParameters;
    private String targetURL;

    public HttpConnection(String targetURL) {
        this.targetURL = targetURL;
    }

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

    public HttpConnection() {
        super();
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

            if (urlParameters != null && !urlParameters.isEmpty()) {
                connection.setRequestProperty("Content-Length", "" +
                        Integer.toString(urlParameters.getBytes().length));
            }
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("charset", "utf-8");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            if (urlParameters != null && !urlParameters.isEmpty()) {
                wr.writeBytes(urlParameters);
            }
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
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    public void call(String url) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Future<Response> response = executor.submit(new Request(new URL(url)));

        InputStream body = response.get().getBody();
        System.out.println("111111");
        executor.shutdown();
    }

    private String convertUrlParameters(Map<String, String> paramsMap) {
        String params = "";
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
//            params += entry.getKey().replaceAll("\\\\", "") + "=" + entry.getValue().replaceAll("\\\\", "") + "&";
            params += entry.getKey() + "=" + entry.getValue() + "&";
        }
        return params;
    }

    public class Request implements Callable<Response> {
        private URL url;

        public Request(URL url) {
            this.url = url;
        }

        @Override
        public Response call() throws Exception {
            return new Response(url.openStream());
        }
    }

    public class Response {
        private InputStream body;

        public Response(InputStream body) {
            System.out.println("HUUUUUUUURAAYYY!!!!");
            this.body = body;
        }

        public InputStream getBody() {
            return body;
        }
    }
}
