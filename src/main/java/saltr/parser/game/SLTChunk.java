/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SLTChunk {
    private List<SLTChunkAssetInfo> chunkAssetInfos;
    private List<SLTCell> chunkCells;
    private List<SLTCell> availableCells;
    private Map assetMap;
    private Map stateMap;

    public SLTChunk(List<SLTChunkAssetInfo> chunkAssetInfos, List<SLTCell> chunkCells, SLTLevelSettings levelSettings) {
        this.chunkCells = chunkCells;
        this.chunkAssetInfos = chunkAssetInfos;

        this.availableCells = new ArrayList<>();
        this.assetMap = levelSettings.getAssetMap();
        this.stateMap = levelSettings.getStateMap();
    }

    public String toString() {
        return "[Chunk] cells:" + availableCells.size() + ", " + " chunkAssets: " + chunkAssetInfos.size();
    }

    public void generate() {
        availableCells = cloneList(chunkCells);
        List<SLTChunkAssetInfo> weakChunkAssetInfos = new ArrayList<>();
        for (SLTChunkAssetInfo assetInfo : this.chunkAssetInfos) {
            if (assetInfo.getCount() != 0) {
                generateAssetInstace(assetInfo.getCount(), assetInfo.getAssetId(), assetInfo.getStateId());
            }
            else {
                weakChunkAssetInfos.add(assetInfo);
            }
        }
        generateWeakAssetsInstances(weakChunkAssetInfos);
    }


    private void generateAssetInstace(int count, String id, String stateId) {
        int randCellIndex;
        SLTCell randCell;
        SLTAsset asset = (SLTAsset) assetMap.get(id);
        String state = (String) stateMap.get(stateId);
        for (int i = 0; i < count; ++i) {
            randCellIndex = (int) (Math.random() * availableCells.size());
            randCell = availableCells.get(randCellIndex);
            randCell.setAssetInstance(new SLTAssetInstance(asset.getKeys(), state, asset.getType()));
            availableCells.subList(randCellIndex, randCellIndex + 1).clear();
            if (availableCells.isEmpty()) {
                return;
            }
        }
    }


    private void generateWeakAssetsInstances(List<SLTChunkAssetInfo> weakChunkAssetInfos) {
        if (!weakChunkAssetInfos.isEmpty()) {
            Integer assetConcentration = availableCells.size() > weakChunkAssetInfos.size() ? availableCells.size() / weakChunkAssetInfos.size() : 1;
            int minAssetCount = assetConcentration <= 2 ? 1 : assetConcentration - 2;
            int maxAssetCount = assetConcentration == 1 ? 1 : assetConcentration + 2;
            int lastChunkAssetIndex = weakChunkAssetInfos.size() - 1;
            SLTChunkAssetInfo chunkAssetInfo;
            int count;
            for (int i = 0; i < weakChunkAssetInfos.size() && !availableCells.isEmpty(); ++i) {
                chunkAssetInfo = weakChunkAssetInfos.get(i);
                count = i == lastChunkAssetIndex ? availableCells.size() : randomWithin(minAssetCount, maxAssetCount);
                generateAssetInstace(count, chunkAssetInfo.getAssetId(), chunkAssetInfo.getStateId());
            }
        }
    }

    private static Integer randomWithin(Integer min, Integer max) {
        return (new Double((Math.random() * (1 + max - min)) + min)).intValue();
    }

    public List<SLTCell> cloneList(List<SLTCell> list) {
        List<SLTCell> clone = new ArrayList(list.size());
        for (SLTCell item : list) clone.add(item.clone());
        return clone;
    }
}
