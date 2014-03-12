/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

public class SLTConfig {
    protected static final String SALTR_API_URL = "http://saltapi.includiv.com/httpjson.action";
    protected static final String SALTR_URL = "http://saltadmin.includiv.com/httpjson.action";

//    protected static final String SALTR_API_URL = "http://10.0.2.2:8081/httpjson.action";
//    protected static final String SALTR_URL = "http://10.0.2.2:8085/httpjson.action";

    protected static final String COMMAND_APP_DATA = "APPDATA";
    protected static final String COMMAND_ADD_PROPERTY = "ADDPROP";
    protected static final String COMMAND_SAVE_OR_UPDATE_FEATURE = "SOUFTR";

    //
    protected static final String APP_DATA_URL_CACHE = "app_data_cache.json";
    protected static final String APP_DATA_URL_INTERNAL = "saltr/app_data.json";
    protected static final String LEVEL_DATA_URL_LOCAL_TEMPLATE = "saltr/pack_{0}/level_{1}.json";
    protected static final String LEVEL_DATA_URL_CACHE_TEMPLATE = "pack_{0}_level_{1}.json";

    public static final String PROPERTY_OPERATIONS_INCREMENT = "inc";
    public static final String PROPERTY_OPERATIONS_SET = "set";

    protected static final String RESULT_SUCCEED = "SUCCEED";
    protected static final String RESULT_ERROR = "FAILURE";
}
