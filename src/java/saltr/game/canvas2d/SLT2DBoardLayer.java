/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTBoardLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a layer of a 2D board.
 */
public class SLT2DBoardLayer extends SLTBoardLayer {
    private List<SLT2DAssetInstance> assetInstances;

    public SLT2DBoardLayer(String layerId, int layerIndex) {
        super(layerId, layerIndex);
        this.assetInstances = new ArrayList<SLT2DAssetInstance>();
    }

    /**
     * @return all the asset instances present on the board.
     */
    public List<SLT2DAssetInstance> getAssetInstances() {
        return assetInstances;
    }

    /**
     * Adds the asset instance to the board.
     *
     * @param instance to be added into "assetInstances" List.
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
