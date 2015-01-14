/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

import java.util.List;
import java.util.Map;

/**
 * The SLTBoard class represents the game board.
 */
public class SLTBoard {
    protected List<SLTBoardLayer> layers;
    private Map<String, Object> properties;

    /**
     * @param layers     The layers of the board.
     * @param properties The board associated properties.
     */
    public SLTBoard(List<SLTBoardLayer> layers, Map<String, Object> properties) {
        this.properties = properties;
        this.layers = layers;
    }

    /**
     * @return The board associated properties.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * @return The layers of the board.
     */
    public List<SLTBoardLayer> getLayers() {
        return layers;
    }

    /**
     * Regenerates the content of all layers.
     */
    public void regenerate() {
        for (SLTBoardLayer layer : layers) {
            layer.regenerate();
        }
    }
}
