/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

public class SLTBoardLayer {
    private String layerId;
    private int layerIndex;

    public SLTBoardLayer(String layerId, int layerIndex) {
        this.layerId = layerId;
        this.layerIndex = layerIndex;
    }

    public String getLayerId() {
        return layerId;
    }

    public int getLayerIndex() {
        return layerIndex;
    }
}
