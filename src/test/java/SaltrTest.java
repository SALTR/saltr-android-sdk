import org.junit.Test;
import saltr.Feature;
import saltr.ObservableSaltr;
import saltr.Saltr;
import saltr.SaltrObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

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

        data.put("3", "3");
        saltr.defineFeature("token2", data);

        saltr.syncFeatures();
    }

    @Override
    public void onGetAppDataSuccess(ObservableSaltr saltr) {

    }

    @Override
    public void onGetAppDataFail(ObservableSaltr saltr) {

    }

    @Override
    public void onGetLevelDataBodySuccess(ObservableSaltr saltr) {

    }

    @Override
    public void onGetLevelDataBodyFail(ObservableSaltr saltr) {

    }

    @Override
    public void onSaveOrUpdateFeatureSuccess(ObservableSaltr saltr) {

    }

    @Override
    public void onSaveOrUpdateFeatureFail(ObservableSaltr saltr) {

    }
}
