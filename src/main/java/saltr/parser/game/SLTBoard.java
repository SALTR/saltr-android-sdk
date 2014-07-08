/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.parser.game;

import java.util.List;
import java.util.Map;

public class SLTBoard {
    private Map<String, String> properties;
    protected List<SLTBoardLayer> layers;

    public SLTBoard(List<SLTBoardLayer> layers, Map<String, String> properties) {
        this.properties = properties;
        this.layers = layers;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public List<SLTBoardLayer> getLayers() {
        return layers;
    }
}
