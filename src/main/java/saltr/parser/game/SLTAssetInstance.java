/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.parser.game;

import java.util.List;

public class SLTAssetInstance {
    protected List<SLTAssetState> states;
    protected Object properties;
    protected String token;

    public SLTAssetInstance(String token, List<SLTAssetState> states, Object properties) {
        this.states = states;
        this.properties = properties;
        this.token = token;
    }

    public List<SLTAssetState> getStates() {
        return states;
    }

    public Object getProperties() {
        return properties;
    }

    public String getToken() {
        return token;
    }
}
