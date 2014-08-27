/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

import java.util.List;
import java.util.Map;

public class SLTBoard {
    private Map<String, Object> properties;
    protected List<SLTBoardLayer> layers;

    public SLTBoard(List<SLTBoardLayer> layers, Map<String, Object> properties) {
        this.properties = properties;
        this.layers = layers;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public List<SLTBoardLayer> getLayers() {
        return layers;
    }

    public void regenerate() {
        for (SLTBoardLayer layer : layers) {
            layer.regenerate();
        }
    }
}
