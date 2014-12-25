/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import java.util.Map;

/**
 * Represents an application feature - a uniquely identifiable set of properties.
 */
public class SLTFeature {
    private String token;
    private Map<String, Object> properties;
    private Boolean required;

    public SLTFeature(String token, Map<String, Object> properties, Boolean required) {
        this.token = token;
        this.properties = properties;
        this.required = required;
    }

    /**
     * @return the token, a unique identifier for a feature.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the user defined properties.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Gets a value indicating whether this {@link saltr.SLTFeature} is required.
     *
     * @return <code>true</code> if required; otherwise, <code>false</code>.
     */
    public Boolean getRequired() {
        return required;
    }

    @Override
    public String toString() {
        return "[SALTR] Feature { token : " + token + ", value : " + properties + "}";
    }
}
