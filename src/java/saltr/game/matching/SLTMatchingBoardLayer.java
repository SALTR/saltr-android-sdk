/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.matching;

import saltr.game.SLTBoardLayer;

import java.util.ArrayList;
import java.util.List;

public class SLTMatchingBoardLayer extends SLTBoardLayer {
    private List<SLTChunk> chunks;

    public SLTMatchingBoardLayer(String layerId, int layerIndex) {
        super(layerId, layerIndex);
        this.chunks = new ArrayList<SLTChunk>();
    }

    public void regenerate() {
        for (SLTChunk chunk : chunks) {
            chunk.generateContent();
        }
    }

    public void addChunk(SLTChunk chunk) {
        chunks.add(chunk);
    }
}
