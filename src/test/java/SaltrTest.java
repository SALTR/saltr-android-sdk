import org.junit.Test;
import saltr.LevelPackStructure;
import saltr.Saltr;
import saltr.SaltrHttpDataHandler;
import saltr.repository.MobileRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */

public class SaltrTest {

    private String instanceKey = "d7c3d370-6238-60f6-d8c5-0ed6a6c745f2";
//    private String instanceKey = "2dc1e2cf-5ed2-54ae-91c4-e781233b9db1";

    @Test
    public void getAppData() {
        final Saltr saltr = Saltr.getSaltr(instanceKey);
        saltr.initDevice("asdhaksjdh", "iphone");
        saltr.initPartner("ajksdhakjsd", "facebook");
        saltr.setRepository(new MobileRepository());

        saltr.setAppVersion("1.0.0");

        Map<String, String> data = new HashMap<String, String>();
        data.put("1", "1");
        data.put("2", "2");
        saltr.defineFeature("token4444444", data);

        data.put("3", "5");
        saltr.defineFeature("token5555555", data);

        saltr.getAppData(new SaltrHttpDataHandler() {
            @Override
            public void onSuccess() {
                List<LevelPackStructure> packs = saltr.getLevelPackStructures();

                saltr.getLevelDataBody(packs.get(0), packs.get(0).getLevelStructureList().get(0), false, new SaltrHttpDataHandler() {

                    @Override
                    public void onSuccess() {
                        System.out.println("LEVEL DATA LOADED");
                    }

                    @Override
                    public void onFail() {
                        System.out.println("LEVEL DATA FAILED TO LOAD");
                    }
                });
                System.out.println("APP DATA LOADED");
            }

            @Override
            public void onFail() {
                System.out.println("APP DATA FAILED TO LOAD");
            }
        });

    }

    @Test
    public void syncFeatures() {
        Saltr saltr = Saltr.getSaltr(instanceKey);
        saltr.setAppVersion("1.0.0");

        Map<String, String> data = new HashMap<String, String>();
        data.put("1", "1");
        data.put("2", "2");
        saltr.defineFeature("token1", data);

        data.put("3", "5");
        saltr.defineFeature("token2", data);

        saltr.syncFeatures();
    }
}
