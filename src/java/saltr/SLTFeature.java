/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import java.util.Map;

public class SLTFeature {
    private String token;
    private Map<String, Object> properties;
    private Boolean required;

    public SLTFeature(String token, Map<String, Object> properties, Boolean required) {
        this.token = token;
        this.properties = properties;
        this.required = required;
    }

    public String getToken() {
        return token;
    }

    public Map<String, Object> getProperties() {
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
