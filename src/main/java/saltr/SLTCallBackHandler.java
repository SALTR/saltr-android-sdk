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
                saltr.appDataLoadSuccessCallback(responseData);
            }
            else if (properties.getDataType().getValue().equals(SLTDataType.LEVEL.getValue())) {
                saltr.loadFromSaltrSuccessCallback(responseData, properties);
            }
            else if (properties.getDataType().getValue().equals(SLTDataType.PLAYER_PROPERTY.getValue())) {
                System.out.println("success");
            }
            else if (properties.getDataType().getValue().equals(SLTDataType.FEATURE.getValue())) {
                System.out.println("[Saltr] Dev feature Sync is complete.");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        if (properties.getDataType().getValue().equals(SLTDataType.APP.getValue())) {
            saltr.appDataLoadFailCallback();
        }
        else if (properties.getDataType().getValue().equals(SLTDataType.LEVEL.getValue())) {
            try {
                saltr.loadFromSaltrFailCallback(properties);
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
