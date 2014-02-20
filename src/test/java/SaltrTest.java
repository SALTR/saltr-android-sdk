import org.junit.Test;
import saltr.AppDataHandler;
import saltr.LevelDataHandler;
import saltr.LevelPackStructure;
import saltr.Saltr;
import saltr.repository.MobileRepository;
import saltr.resource.HttpConnection;

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

    @Test
    public void getAppData() {
//        Saltr saltr = new Saltr("2dc1e2cf-5ed2-54ae-91c4-e781233b9db1");
        Saltr saltr = new Saltr(instanceKey);
        saltr.initDevice("asdhaksjdh", "iphone");
        saltr.initPartner("ajksdhakjsd", "facebook");
        saltr.setRepository(new MobileRepository());

        saltr.getAppData(new AppDataHandler() {
            @Override
            public void onGetAppDataSuccess() {
                System.out.println("APP DATA LOADED");
            }

            @Override
            public void onGetAppDataFail() {
                System.out.println("APP DATA FAILED TO LOAD");
            }
        });
        List<LevelPackStructure> packs = saltr.getLevelPackStructures();

        saltr.getLevelDataBody(packs.get(0), packs.get(0).getLevelStructureList().get(0), false, new LevelDataHandler() {

            @Override
            public void onGetLevelDataBodySuccess() {
                System.out.println("LEVEL DATA LOADED");
            }

            @Override
            public void onGetLevelDataBodyFail() {
                System.out.println("LEVEL DATA FAILED TO LOAD");
            }
        });
    }

    @Test
    public void syncFeatures() {
        Saltr saltr = new Saltr(instanceKey);
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
    public void asynchCall() throws Exception {
        HttpConnection connection = new HttpConnection("http://localadmin.saltr.com:8085/", new HashMap<String, String>());
        connection.call("http://localapi.saltr.com:8081/httpjson.action?command=APPDATA&arguments={\"instanceKey\":\"d7c3d370-6238-60f6-d8c5-0ed6a6c745f2\",\"partner\":{\"partnerId\":\"100000024783448\",\"partnerType\":\"facebook\",\"gender\":\"male\",\"age\":36,\"firstName\":\"Artem\",\"lastName\":\"Sukiasyan\"},\"device\":{\"deviceId\":\"asdas123kasd\",\"deviceType\":\"iphone\"}}");
//        connection.call("http://localadmin.saltr.com:8085/");
    }
}
