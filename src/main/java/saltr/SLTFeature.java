/*
 * Copyright (c) 2014 Plexonic Ltd
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
