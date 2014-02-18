import com.ning.http.client.FluentStringsMap;
import org.junit.Test;
import saltr.ObservableSaltr;
import saltr.Saltr;
import saltr.SaltrObserver;
import saltr.resource.HttpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */

public class SaltrTest implements SaltrObserver {

    private String instanceKey = "d7c3d370-6238-60f6-d8c5-0ed6a6c745f2";

    @Test
    public void gettingAppData() {
//        Saltr saltr = new Saltr("2dc1e2cf-5ed2-54ae-91c4-e781233b9db1");
        Saltr saltr = new Saltr(instanceKey);
        saltr.addObserver(this);
        saltr.initDevice("asdhaksjdh", "iphone");
        saltr.initPartner("ajksdhakjsd", "facebook");

        saltr.getAppData();
    }

    @Test
    public void syncFeatures() {
        Saltr saltr = new Saltr(instanceKey);
        saltr.addObserver(this);
        saltr.setAppVersion("1.0.0");

        Map<String, String> data = new HashMap<String, String>();
        data.put("1", "1");
        data.put("2", "2");
        saltr.defineFeature("token1", data);

        data.put("3", "5");
        saltr.defineFeature("token2", data);

        saltr.syncFeatures();
    }

    @Test
    public void asynchCall() throws IOException, ExecutionException, InterruptedException {
        HttpConnection connection = new HttpConnection("http://localadmin.saltr.com:8085/", new HashMap<String, String>());
        FluentStringsMap map = new FluentStringsMap();
        map.put("command", new ArrayList<String>());
        map.put("instanceKey", new ArrayList<String>());
        map.put("data", new ArrayList<String>());
        connection.call("http://localadmin.saltr.com:8085/", map);
    }

    @Override
    public void onGetAppDataSuccess(ObservableSaltr saltr) {
        System.out.println("onGetAppDataSuccess");
    }

    @Override
    public void onGetAppDataFail(ObservableSaltr saltr) {
        System.out.println("onGetAppDataFail");
    }

    @Override
    public void onGetLevelDataBodySuccess(ObservableSaltr saltr) {
        System.out.println("onGetLevelDataBodySuccess");
    }

    @Override
    public void onGetLevelDataBodyFail(ObservableSaltr saltr) {
        System.out.println("onGetLevelDataBodyFail");
    }

    @Override
    public void onSaveOrUpdateFeatureSuccess(ObservableSaltr saltr) {
        System.out.println("onSaveOrUpdateFeatureSuccess");
    }

    @Override
    public void onSaveOrUpdateFeatureFail(ObservableSaltr saltr) {
        System.out.println("onSaveOrUpdateFeatureFail");
    }
}
