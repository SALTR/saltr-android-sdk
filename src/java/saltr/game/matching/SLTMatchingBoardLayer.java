/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.matching;

import saltr.game.SLTBoardLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a layer of a matching board.
 */
public class SLTMatchingBoardLayer extends SLTBoardLayer {
    private List<SLTChunk> chunks;

    public SLTMatchingBoardLayer(String layerId, int layerIndex) {
        super(layerId, layerIndex);
        this.chunks = new ArrayList<SLTChunk>();
    }

    /**
     * Regenerates contents of all the chunks within the layer.
     */
    public void regenerate() {
        for (SLTChunk chunk : chunks) {
            chunk.generateContent();
        }
    }

    /**
     * Adds a chunk to the layer.
     *
     * @param chunk to add.
     */
    public void addChunk(SLTChunk chunk) {
        chunks.add(chunk);
    }
}
