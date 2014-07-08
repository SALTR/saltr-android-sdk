/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.parser.response.level;

import java.util.List;

public class SLTResponseBoardLayer {
    private String layerId;
    private List<SLTResponseBoardChunk> chunks;
    private List<SLTResponseBoardFixedAsset> fixedAssets;

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public List<SLTResponseBoardChunk> getChunks() {
        return chunks;
    }

    public void setChunks(List<SLTResponseBoardChunk> chunks) {
        this.chunks = chunks;
    }

    public List<SLTResponseBoardFixedAsset> getFixedAssets() {
        return fixedAssets;
    }

    public void setFixedAssets(List<SLTResponseBoardFixedAsset> fixedAssets) {
        this.fixedAssets = fixedAssets;
    }
}
