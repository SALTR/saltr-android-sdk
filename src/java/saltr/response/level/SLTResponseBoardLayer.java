/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response.level;

import java.io.Serializable;
import java.util.List;

public class SLTResponseBoardLayer implements Serializable {
    private String token;
    private List<SLTResponseBoardChunk> chunks;
    private List<SLTResponseBoardFixedAsset> fixedAssets;
    private List<SLTResponseBoardChunkAsset> assets;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<SLTResponseBoardChunkAsset> getAssets() {
        return assets;
    }

    public void setAssets(List<SLTResponseBoardChunkAsset> assets) {
        this.assets = assets;
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
