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
    private Boolean required;

    public SLTFeature(String token, Map<String, String> properties, Boolean required) {
        this.token = token;
        this.properties = properties;
        this.required = required;
    }

    public String getToken() {
        return token;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Boolean getRequired() {
        return required;
    }

    @Override
    public String toString() {
        return "[SALTR] Feature { token : " + token + ", value : " + properties + "}";
    }
}
