/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

import java.util.List;

/**
 * Represents a particular instance of an asset, placed on board.
 */
public class SLTAssetInstance {
    protected List<SLTAssetState> states;
    protected Object properties;
    protected String token;

    public SLTAssetInstance(String token, List<SLTAssetState> states, Object properties) {
        this.states = states;
        this.properties = properties;
        this.token = token;
    }

    /**
     * @return the states the instance is in.
     */
    public List<SLTAssetState> getStates() {
        return states;
    }

    /**
     * @return the asset properties.
     */
    public Object getProperties() {
        return properties;
    }

    /**
     * @return the token, a unique identifier for each  asset, not unique for a particular instance.
     */
    public String getToken() {
        return token;
    }
}
