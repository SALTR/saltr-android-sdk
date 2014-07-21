/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

public class SLTAssetState {
    private String token;
    private Object properties;

    public SLTAssetState(String token, Object properties) {
        this.token = token;
        this.properties = properties;
    }

    public String getToken() {
        return token;
    }

    public Object getProperties() {
        return properties;
    }
}
