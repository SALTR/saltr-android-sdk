/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

import saltr.parser.response.level.SLTResponseBoardChunk;

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
