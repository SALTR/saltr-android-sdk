/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import java.util.Map;

public class SLTFeature {
    private String token;
    private Map<String, String> properties;
    private Map<String, String> defaultProperties;

    public SLTFeature(String token, Map<String, String> properties) {
        this.token = token;
        this.properties = properties;
    }

    public SLTFeature(String token, Map<String, String> properties, Map<String, String> defaultProperties) {
        this.token = token;
        this.properties = properties;
        this.defaultProperties = defaultProperties;
    }

    public String getToken() {
        return token;
    }

    public Map<String, String> getDefaultProperties() {
        return defaultProperties;
    }

    public Map<String, String> getProperties() {
        return properties == null ? defaultProperties : properties;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void setDefaultProperties(Map<String, String> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }
}
