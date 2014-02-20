/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicResponseHandler;

public class SaltrCallBack implements FutureCallback<HttpResponse> {

    private Saltr saltr;
    private CallBackDetails details;

    public SaltrCallBack(Saltr saltr, CallBackDetails details) {
        this.saltr = saltr;
        this.details = details;
    }

    public void completed(HttpResponse response) {
        try {
            String responseData = new BasicResponseHandler().handleResponse(response);
            if (details.getDataType().getValue().equals(DataType.APP.getValue())) {
                saltr.appDataAssetLoadCompleteHandler(responseData);
            }
            else {
                saltr.levelDataAssetLoadedHandler(responseData, details);
            }
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void failed(Exception ex) {
        if (details.getDataType().getValue().equals(DataType.APP.getValue())) {
            saltr.appDataAssetLoadErrorHandler();
        }
        else {
            saltr.levelDataAssetLoadErrorHandler(details);
        }
    }

    public void cancelled() {
    }


}
