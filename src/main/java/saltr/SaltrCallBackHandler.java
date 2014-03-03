/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public class SaltrCallBackHandler extends AsyncHttpResponseHandler {

    private Saltr saltr;
    private CallBackDetails details;

    public SaltrCallBackHandler(Saltr saltr, CallBackDetails details) {
        this.saltr = saltr;
        this.details = details;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            String responseData = new String(responseBody, "UTF-8");
            if (details.getDataType().getValue().equals(DataType.APP.getValue())) {
                saltr.appDataAssetLoadCompleteHandler(responseData);
            }
            else if (details.getDataType().getValue().equals(DataType.LEVEL.getValue())) {
                saltr.levelDataAssetLoadedHandler(responseData, details);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        if (details.getDataType().getValue().equals(DataType.APP.getValue())) {
            saltr.appDataAssetLoadErrorHandler();
        }
        else if (details.getDataType().getValue().equals(DataType.LEVEL.getValue())) {
            saltr.levelDataAssetLoadErrorHandler(details);
        }
    }
}
