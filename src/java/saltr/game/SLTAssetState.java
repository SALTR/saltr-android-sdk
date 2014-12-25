/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

/**
 * Represents a state of an asset.
 */
public class SLTAssetState {
    private String token;
    private Object properties;

    public SLTAssetState(String token, Object properties) {
        this.token = token;
        this.properties = properties;
    }

    /**
     * @return the token, a unique identifier for each state of an asset.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the properties, associated with the state.
     */
    public Object getProperties() {
        return properties;
    }
}
