/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTBoardLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * The SLT2DBoardLayer class represents the game 2D board's layer.
 */
public class SLT2DBoardLayer extends SLTBoardLayer {
    private List<SLT2DAssetInstance> assetInstances;

    /**
     * @param layerId    The layer's identifier.
     * @param layerIndex The layer's ordering index.
     */
    public SLT2DBoardLayer(String layerId, int layerIndex) {
        super(layerId, layerIndex);
        this.assetInstances = new ArrayList<SLT2DAssetInstance>();
    }

    /**
     * @return The asset instances of the layer.
     */
    public List<SLT2DAssetInstance> getAssetInstances() {
        return assetInstances;
    }

    /**
     * Adds an asset instance.
     *
     * @param instance An asset instance to add.
     */
    public void addAssetInstance(SLT2DAssetInstance instance) {
        assetInstances.add(instance);
    }

    /**
     * Currently has no effect, since 2D boards do not have any randomized content.
     */
    @Override
    public void regenerate() {
        // nothing to do here yet.
    }
}
