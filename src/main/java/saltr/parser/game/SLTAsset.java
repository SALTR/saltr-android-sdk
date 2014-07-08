/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.parser.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SLTAsset {
    protected Object properties;
    protected Map<String, SLTAssetState> stateMap;
    protected String token;

    public SLTAsset(String token, Map<String, SLTAssetState> stateMap, Object properties) {
        this.properties = properties;
        this.stateMap = stateMap;
        this.token = token;
    }

    public Object getProperties() {
        return properties;
    }

    public String getToken() {
        return token;
    }

    public String toString() {
        return "[Asset] token: " + token + ", " + " properties: " + properties;
    }

    public SLTAssetInstance getInstance(List<String> stateIds) {
        return new SLTAssetInstance(token, getInstanceStates(stateIds), properties);
    }

    protected List<SLTAssetState> getInstanceStates(List<String> stateIds) {
        List<SLTAssetState> states = new ArrayList<>();
        for (String stateId : stateIds) {
            SLTAssetState state = stateMap.get(stateId);
            if (state != null) {
                states.add(state);
            }
        }
        return states;
    }
}
