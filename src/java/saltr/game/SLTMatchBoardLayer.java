/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

import java.util.ArrayList;
import java.util.List;

public class SLTMatchBoardLayer extends SLTBoardLayer {
    private List<SLTChunk> chunks;

    public SLTMatchBoardLayer(String layerId, int layerIndex) {
        super(layerId, layerIndex);
        this.chunks = new ArrayList<>();
    }

    public void regenerateChunks() {
        for (SLTChunk chunk : chunks) {
            chunk.generateContent();
        }
    }

    public void addChunk(SLTChunk chunk) {
        chunks.add(chunk);
    }
}
