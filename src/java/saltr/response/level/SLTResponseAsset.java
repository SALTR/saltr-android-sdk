/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response.level;

import java.io.Serializable;
import java.util.Map;

public class SLTResponseAsset implements Serializable {
    private Map<String, Integer> keys;
    private String type;
    private String token;
    private Map<String, SLTResponseBoardChunkAssetState> states;
    private Object cells;
    private Object properties;

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    public Map<String, Integer> getKeys() {
        return keys;
    }

    public void setKeys(Map<String, Integer> keys) {
        this.keys = keys;
    }

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getCells() {
        return cells;
    }

    public void setCells(Object cells) {
        this.cells = cells;
    }

    public Map<String, SLTResponseBoardChunkAssetState> getStates() {
        return states;
    }

    public void setStates(Map<String, SLTResponseBoardChunkAssetState> states) {
        this.states = states;
    }
}
