/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.gameeditor.chunk;


import saltr.parser.gameeditor.BoardData;
import saltr.parser.gameeditor.Cell;
import saltr.parser.gameeditor.simple.SimpleAsset;
import saltr.parser.gameeditor.simple.SimpleAssetTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chunk {
    private String id;
    private List<AssetInChunk> chunkAssets;
    private List items;
    private List<Cell> cells;
    private Object[][] outputBoard;
    private Map boardAssetMap;
    private Map boardStateMap;

    public Chunk(String id, Object[][] outputBoard, BoardData boardData) {
        this.id = id;
        this.chunkAssets = new ArrayList<AssetInChunk>();
        this.cells = new ArrayList<Cell>();
        this.items = new ArrayList();
        this.outputBoard = outputBoard;
        this.boardAssetMap = boardData.getAssetMap();
        this.boardStateMap = boardData.getStateMap();
    }

    public void generate() {
        List<AssetInChunk> weakChunkAsset = new ArrayList<AssetInChunk>();
        for (AssetInChunk chunkAsset : this.chunkAssets) {
            if (chunkAsset.getCount() != 0) {
                generateAsset(chunkAsset.getCount(), chunkAsset.getId(), chunkAsset.getStateId());
            }
            else {
                weakChunkAsset.add(chunkAsset);
            }
        }
        generateWeakAssets(weakChunkAsset);
    }


    private void generateAsset(int count, String id, String stateId) {
        int randCellIndex;
        Cell randCell;
        SimpleAsset asset;
        SimpleAssetTemplate assetTemplate = (SimpleAssetTemplate) boardAssetMap.get(id);
        String state = (String) boardStateMap.get(stateId);
        for (int i = 0; i < count; ++i) {
            randCellIndex = (int) (Math.random() * cells.size());
            randCell = cells.get(randCellIndex);
            asset = new SimpleAsset();
            asset.setKeys(assetTemplate.getKeys());
            asset.setState(state);
            asset.setType(assetTemplate.getType());
            asset.setCell(randCell);
            outputBoard[randCell.getX()][randCell.getY()] = asset;
            cells.subList(randCellIndex, 1).clear();
        }
        if (cells.isEmpty()) {
            return;
        }
    }


    private void generateWeakAssets(List<AssetInChunk> weakChunkAsset) {
        if (!weakChunkAsset.isEmpty()) {
            Integer assetConcentration = cells.size() > weakChunkAsset.size() ? cells.size() / weakChunkAsset.size() : 1;
            int minAssetCount = assetConcentration <= 2 ? 1 : assetConcentration - 2;
            int maxAssetCount = assetConcentration == 1 ? 1 : assetConcentration + 2;
            int lastChunkAssetIndex = weakChunkAsset.size() - 1;
            AssetInChunk chunkAsset;
            int count;
            for (int i = 0; i < weakChunkAsset.size() && !cells.isEmpty(); ++i) {
                chunkAsset = weakChunkAsset.get(i);
                count = i == lastChunkAssetIndex ? cells.size() : randomWithin(minAssetCount, maxAssetCount);
                generateAsset(count, chunkAsset.getId(), chunkAsset.getStateId());
            }
        }
    }

    public void addChunkAsset(AssetInChunk chunkAsset) {
        chunkAssets.add(chunkAsset);
    }

    public String toString() {
        return "Chunk : [cells :" + cells + " ]" + "[chunkAssets : " + chunkAssets + " ]";
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        id = value;
    }

    public void addItem(Object item) {
        items.add(item);
    }

    private static Integer randomWithin(Integer min, Integer max) {
        return (new Double((Math.random() * (1 + max - min)) + min)).intValue();
    }

    public List<AssetInChunk> getChunkAssets() {
        return chunkAssets;
    }
}
