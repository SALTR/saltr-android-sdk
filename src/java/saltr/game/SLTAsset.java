/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The SLTAsset class represents the game asset.
 */
public class SLTAsset {
    protected Object properties;
    protected Map<String, SLTAssetState> stateMap;
    protected String token;

    /**
     * @param token      The unique identifier of the asset.
     * @param stateMap   The states.
     * @param properties The properties.
     */
    public SLTAsset(String token, Map<String, SLTAssetState> stateMap, Object properties) {
        this.properties = properties;
        this.stateMap = stateMap;
        this.token = token;
    }

    /**
     * @return The properties.
     */
    public Object getProperties() {
        return properties;
    }

    /**
     *
     * @return The unique identifier of the asset.
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @return token plus properties string.
     */
    public String toString() {
        return "[Asset] token: " + token + ", " + " properties: " + properties;
    }

    /**
     *
     * @param stateIds The state identifiers.
     * @return instance states by provided state identifiers.
     */
    public List<SLTAssetState> getInstanceStates(List<String> stateIds) {
        List<SLTAssetState> states = new ArrayList<SLTAssetState>();
        for (String stateId : stateIds) {
            SLTAssetState state = stateMap.get(stateId);
            if (state != null) {
                states.add(state);
            }
        }
        return states;
    }
}
