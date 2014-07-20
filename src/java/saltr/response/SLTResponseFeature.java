/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response;

import java.io.Serializable;
import java.util.Map;

public class SLTResponseFeature implements Serializable {
    private String token;
    private Map<String, String> data;
    private Map<String, String> properties;
    private Boolean required;

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
