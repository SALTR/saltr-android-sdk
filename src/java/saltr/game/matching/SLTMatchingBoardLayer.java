/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.matching;

import saltr.game.SLTBoardLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * The SLTMatchingBoardLayer class represents the matching board.
 */
public class SLTMatchingBoardLayer extends SLTBoardLayer {
    private List<SLTChunk> chunks;

    /**
     * @param layerId    The layer's identifier.
     * @param layerIndex The layer's index.
     */
    public SLTMatchingBoardLayer(String layerId, int layerIndex) {
        super(layerId, layerIndex);
        this.chunks = new ArrayList<SLTChunk>();
    }

    /**
     * Regenerates the content of the layer.
     */
    public void regenerate() {
        for (SLTChunk chunk : chunks) {
            chunk.generateContent();
        }
    }

    /**
     * Adds a chunk.
     * @param chunk The chunk to add.
     */
    public void addChunk(SLTChunk chunk) {
        chunks.add(chunk);
    }
}
