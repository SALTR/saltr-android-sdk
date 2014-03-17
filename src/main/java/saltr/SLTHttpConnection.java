/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SLTHttpConnection {
    private String url;
    private RequestParams params;

    public SLTHttpConnection(String targetURL) {
        this.url = targetURL;
    }

    public SLTHttpConnection(String targetURL, RequestParams params) {
        this.url = targetURL;
        this.params = params;
    }

    public void call(SLTSaltr saltr, SLTCallBackProperties properties) throws Exception {
        AsyncHttpClient client = new AsyncHttpClient();
        AsyncHttpResponseHandler callBack = new SLTCallBackHandler(saltr, properties);
        client.post(url, params, callBack);
    }
}
