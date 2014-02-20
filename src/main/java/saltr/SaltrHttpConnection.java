/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SaltrHttpConnection {
    private String url;
    private List<NameValuePair> parameters;

    public SaltrHttpConnection(String targetURL) {
        this.url = targetURL;
    }

    public SaltrHttpConnection(String targetURL, List<NameValuePair> parameters) {
        this.url = targetURL;
        this.parameters = parameters;
    }

    public void call(Saltr saltr, CallBackDetails details) throws Exception {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(3000)
                .setConnectTimeout(3000).build();
        CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        try {
            httpClient.start();
            final HttpPost request = new HttpPost(url);
            if (parameters != null) {
                request.setEntity(new UrlEncodedFormEntity(parameters));
            }
            FutureCallback<HttpResponse> callBack = new SaltrCallBack(saltr, details);
            final CountDownLatch latch = new CountDownLatch(1);
            httpClient.execute(request, callBack);
            latch.await();
        } finally {
            httpClient.close();
        }
    }
}
