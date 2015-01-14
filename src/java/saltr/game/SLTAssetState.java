/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

/**
 * The SLTAssetState class represents the asset state and provides the state related properties.
 */
public class SLTAssetState {
    private String token;
    private Object properties;

    /**
     * @param token      The unique identifier of the state.
     * @param properties The current state related properties.
     */
    public SLTAssetState(String token, Object properties) {
        this.token = token;
        this.properties = properties;
    }

    /**
     * @return The unique identifier of the state.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return The properties of the state.
     */
    public Object getProperties() {
        return properties;
    }
}
