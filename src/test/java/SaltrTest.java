import org.junit.Test;
import saltr.ObservableSaltr;
import saltr.Saltr;
import saltr.SaltrObserver;

import java.util.Observable;
import java.util.Observer;

/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */

public class SaltrTest implements SaltrObserver {

    @Test
    public void gettingAppData() {
//        Saltr saltr = new Saltr("2dc1e2cf-5ed2-54ae-91c4-e781233b9db1");
        Saltr saltr = new Saltr("d7c3d370-6238-60f6-d8c5-0ed6a6c745f2");
        saltr.addObserver(this);
        saltr.initDevice("asdhaksjdh", "iphone");
        saltr.initPartner("ajksdhakjsd", "facebook");

        saltr.getAppData();
    }

    @Test
    public void test() {

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
