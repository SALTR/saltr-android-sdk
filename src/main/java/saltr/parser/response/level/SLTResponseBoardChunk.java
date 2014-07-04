/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.response.level;

import java.util.List;

public class SLTResponseBoardChunk {
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
