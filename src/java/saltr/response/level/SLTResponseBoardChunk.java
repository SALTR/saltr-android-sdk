/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response.level;

import java.io.Serializable;
import java.util.List;

public class SLTResponseBoardChunk implements Serializable {
    private Long chunkId;
    private List<SLTResponseBoardChunkAsset> assets;
    private List<List<Integer>> cells;

    public Long getChunkId() {
        return chunkId;
    }

    public void setChunkId(Long chunkId) {
        this.chunkId = chunkId;
    }

    public List<SLTResponseBoardChunkAsset> getAssets() {
        return assets;
    }

    public void setAssets(List<SLTResponseBoardChunkAsset> assets) {
        this.assets = assets;
    }

    public List<List<Integer>> getCells() {
        return cells;
    }

    public void setCells(List<List<Integer>> cells) {
        this.cells = cells;
    }
}
