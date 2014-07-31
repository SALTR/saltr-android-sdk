/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

public class SLTConfig {
    public static final String CMD_APP_DATA = "getAppData";
    public static final String CMD_ADD_PROPERTIES = "addProperties";
    public static final String CMD_DEV_SYNC_FEATURES = "syncFeatures";

    public static final String SALTR_API_URL = "https://api.saltr.com/call";
    public static final String SALTR_DEVAPI_URL = "https://devapi.saltr.com/call";

    public static final String APP_DATA_URL_CACHE = "app_data_cache.json";
    public static final String LOCAL_LEVELPACK_PACKAGE_URL = "saltr/level_packs.json";
    public static final String LOCAL_LEVEL_CONTENT_PACKAGE_URL_TEMPLATE = "saltr/pack_{0}/level_{1}.json";
    public static final String LOCAL_LEVEL_CONTENT_CACHE_URL_TEMPLATE = "pack_{0}_level_{1}.json";

    protected static final String RESULT_SUCCEED = "SUCCEED";
    protected static final String RESULT_ERROR = "FAILED";
}