/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

import java.util.List;

/**
 * The SLTAssetInstance class represents the game asset instance placed on board.
 * It holds the unique identifier of the asset and current instance related states and properties.
 */
public class SLTAssetInstance {
    protected List<SLTAssetState> states;
    protected Object properties;
    protected String token;

    /**
     * @param token      The unique identifier of the asset.
     * @param states     The current instance states.
     * @param properties The current instance properties.
     */
    public SLTAssetInstance(String token, List<SLTAssetState> states, Object properties) {
        this.states = states;
        this.properties = properties;
        this.token = token;
    }

    /**
     * @return The current instance states.
     */
    public List<SLTAssetState> getStates() {
        return states;
    }

    /**
     * @return The current instance properties.
     */
    public Object getProperties() {
        return properties;
    }

    /**
     * @return The unique identifier of the asset.
     */
    public String getToken() {
        return token;
    }
}
