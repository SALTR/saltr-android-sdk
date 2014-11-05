/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

public class SLTConfig {
    public static final String ACTION_GET_APP_DATA = "getAppData";
    public static final String ACTION_ADD_PROPERTIES = "addProperties";
    public static final String ACTION_DEV_SYNC_FEATURES = "syncFeatures";
    public static final String ACTION_DEV_REGISTER_IDENTITY = "registerIdentity";
    public static final String SALTR_API_URL = "http://saltapi.includiv.com/call";
    public static final String SALTR_DEVAPI_URL = "http://saltapp.includiv.com/call";

    public static final String APP_DATA_URL_CACHE = "app_data_cache.json";
    public static final String LOCAL_LEVELPACK_PACKAGE_URL = "saltr/level_packs.json";
    public static final String LOCAL_LEVEL_CONTENT_PACKAGE_URL_TEMPLATE = "saltr/pack_{0}/level_{1}.json";
    public static final String LOCAL_LEVEL_CONTENT_CACHE_URL_TEMPLATE = "pack_{0}_level_{1}.json";

    public static final String DEVICE_TYPE_IPAD = "ipad";
    public static final String DEVICE_TYPE_IPHONE = "iphone";
    public static final String DEVICE_TYPE_IPOD = "ipod";
    public static final String DEVICE_TYPE_ANDROID = "android";
    public static final String DEVICE_PLATFORM_ANDROID = "android";
    public static final String DEVICE_PLATFORM_IOS = "ios";
}
