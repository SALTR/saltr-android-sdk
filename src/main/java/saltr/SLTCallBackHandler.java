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

public class SLTCallBackHandler extends AsyncHttpResponseHandler {

    private SLTSaltr saltr;
    private SLTCallBackProperties properties;

    public SLTCallBackHandler(SLTSaltr saltr, SLTCallBackProperties properties) {
        this.saltr = saltr;
        this.properties = properties;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            String responseData = new String(responseBody, "UTF-8");
            if (properties.getDataType().getValue().equals(SLTDataType.APP.getValue())) {
                saltr.appDataLoadCompleteCallback(responseData);
            }
            else if (properties.getDataType().getValue().equals(SLTDataType.LEVEL.getValue())) {
                saltr.loadSuccessCallback(responseData, properties);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        if (properties.getDataType().getValue().equals(SLTDataType.APP.getValue())) {
            saltr.appDataLoadFailedCallback();
        }
        else if (properties.getDataType().getValue().equals(SLTDataType.LEVEL.getValue())) {
            saltr.loadFailedCallback(properties);
        }
    }
}
