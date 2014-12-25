/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

import java.util.List;
import java.util.Map;

/**
 * Represents any kind of a board.
 */
public class SLTBoard {
    protected List<SLTBoardLayer> layers;
    private Map<String, Object> properties;

    public SLTBoard(List<SLTBoardLayer> layers, Map<String, Object> properties) {
        this.properties = properties;
        this.layers = layers;
    }

    /**
     * @return the properties associated with the board.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * @return the layers of the board.
     */
    public List<SLTBoardLayer> getLayers() {
        return layers;
    }

    /**
     * Regenerates contents of all layers.
     */
    public void regenerate() {
        for (SLTBoardLayer layer : layers) {
            layer.regenerate();
        }
    }
}
