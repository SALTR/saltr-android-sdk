/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTBoardLayer;

import java.util.ArrayList;
import java.util.List;

public class SLT2DBoardLayer extends SLTBoardLayer {
    private List<SLT2DAssetInstance> assetInstances;

    public SLT2DBoardLayer(String layerId, int layerIndex) {
        super(layerId, layerIndex);
        this.assetInstances = new ArrayList<SLT2DAssetInstance>();
    }

    public List<SLT2DAssetInstance> getAssetInstances() {
        return assetInstances;
    }

    public void addAssetInstance(SLT2DAssetInstance instance) {
        assetInstances.add(instance);
    }

    @Override
    public void regenerate() {

    }
}
