/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.parser.response.level;

import java.util.List;
import java.util.Map;

public class SLTResponseAsset {
    private Map<String, Integer> keys;
    private String type;
    private String token;
    private List<SLTResponseBoardChunkAssetState> states;
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

    public List<SLTResponseBoardChunkAssetState> getStates() {
        return states;
    }

    public void setStates(List<SLTResponseBoardChunkAssetState> states) {
        this.states = states;
    }
}
