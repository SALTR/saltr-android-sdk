/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

import saltr.parser.response.level.SLTResponseBoardChunk;

import java.util.List;

public class SLTLevelBoardLayer {
    private String layerId;
    private int layerIndex;
    private List fixedAssetsNodes;
    private List<SLTResponseBoardChunk> chunkNodes;

    public SLTLevelBoardLayer(String layerId, int layerIndex, List fixedAssetsNodes, List<SLTResponseBoardChunk> chunkNodes) {
        this.layerId = layerId;
        this.layerIndex = layerIndex;
        this.fixedAssetsNodes = fixedAssetsNodes;
        this.chunkNodes = chunkNodes;
    }

    public String getLayerId() {
        return layerId;
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public List getFixedAssetsNodes() {
        return fixedAssetsNodes;
    }

    public List<SLTResponseBoardChunk> getChunkNodes() {
        return chunkNodes;
    }
}
