/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.matching;


import saltr.game.SLTAsset;
import saltr.game.SLTAssetInstance;

import java.util.*;

public class SLTChunk {
    private String layerToken;
    private int layerIndex;
    private List<SLTChunkAssetRule> chunkAssetRules;
    private List<SLTCell> chunkCells;
    private List<SLTCell> availableCells;
    private Map<String, SLTAsset> assetMap;

    private static Integer randomWithin(Integer min, Integer max) {
        return (new Double((Math.random() * (1 + max - min)) + min)).intValue();
    }

    public SLTChunk(String layerToken, int layerIndex, List<SLTCell> chunkCells, List<SLTChunkAssetRule> chunkAssetRules, Map<String, SLTAsset> assetMap) {
        this.layerToken = layerToken;
        this.layerIndex = layerIndex;
        this.chunkCells = chunkCells;
        this.chunkAssetRules = chunkAssetRules;
        this.assetMap = assetMap;
    }

    public String toString() {
        return "[Chunk] cells:" + availableCells.size() + ", " + " chunkAssets: " + chunkAssetRules.size();
    }

    public void generateContent() {
        resetChunkCells();

        availableCells = cloneList(chunkCells);

        List<SLTChunkAssetRule> countChunkAssetRules = new ArrayList<SLTChunkAssetRule>();
        List<SLTChunkAssetRule> ratioChunkAssetRules = new ArrayList<SLTChunkAssetRule>();
        List<SLTChunkAssetRule> randomChunkAssetRules = new ArrayList<SLTChunkAssetRule>();

        for (SLTChunkAssetRule assetRule : this.chunkAssetRules) {
            if (assetRule.getDistributionType().equals("count")) {
                countChunkAssetRules.add(assetRule);
            }
            else if (assetRule.getDistributionType().equals("ratio")) {
                ratioChunkAssetRules.add(assetRule);
            }
            else if (assetRule.getDistributionType().equals("random")) {
                randomChunkAssetRules.add(assetRule);
            }
        }

        if (!countChunkAssetRules.isEmpty()) {
            generateAssetInstancesByCount(countChunkAssetRules);
        }
        if (!ratioChunkAssetRules.isEmpty()) {
            generateAssetInstancesByRatio(ratioChunkAssetRules);
        }
        else if (!randomChunkAssetRules.isEmpty()) {
            generateAssetInstancesRandomly(randomChunkAssetRules);
        }
    }

    private void resetChunkCells() {
        for (SLTCell chunkCell : chunkCells) {
            chunkCell.removeAssetInstance(layerToken, layerIndex);
        }
    }

    private void generateAssetInstancesByCount(List<SLTChunkAssetRule> countChunkAssetRules) {
        for (SLTChunkAssetRule assetRule : countChunkAssetRules) {
            generateAssetInstances(assetRule.getDistributionValue(), assetRule.getAssetId(), assetRule.getStateIds());
        }
    }

    private void generateAssetInstancesByRatio(List<SLTChunkAssetRule> ratioChunkAssetRules) {
        float ratioSum = 0;
        for (SLTChunkAssetRule assetRule : ratioChunkAssetRules) {
            ratioSum += assetRule.getDistributionValue();
        }
        int availableCellsNum = availableCells.size();
        List<Map<String, Object>> fractionAssets = new ArrayList<Map<String, Object>>();
        if (ratioSum != 0) {
            for (SLTChunkAssetRule assetRule : ratioChunkAssetRules) {
                float proportion = assetRule.getDistributionValue() / ratioSum * availableCellsNum;
                int count = (int) Math.floor(proportion);
                Map<String, Object> fractionAsset = new HashMap<String, Object>();
                fractionAsset.put("fraction", (int) (proportion - count));
                fractionAsset.put("assetRule", assetRule);
                fractionAssets.add(fractionAsset);
                generateAssetInstances(count, assetRule.getAssetId(), assetRule.getStateIds());
            }

            Comparator<Map<String, Object>> comparator = new Comparator<Map<String, Object>>() {
                public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                    Integer i1 = Integer.valueOf(m1.get("fraction").toString());
                    Integer i2 = Integer.valueOf(m2.get("fraction").toString());
                    return i2.compareTo(i1);
                }
            };

            Collections.sort(fractionAssets, comparator);
            availableCellsNum = availableCells.size();

            for (int k = 0; k < availableCellsNum; k++) {
                SLTChunkAssetRule rule = (SLTChunkAssetRule) fractionAssets.get(k).get("assetRule");
                generateAssetInstances(1, rule.getAssetId(), rule.getStateIds());
            }
        }
    }

    private void generateAssetInstancesRandomly(List<SLTChunkAssetRule> randomChunkAssetRules) {
        int len = randomChunkAssetRules.size();
        int availableCellsNum = availableCells.size();

        if (len > 0) {
            Integer assetConcentration = availableCellsNum > len ? availableCellsNum / len : 1;
            int minAssetCount = assetConcentration <= 2 ? 1 : assetConcentration - 2;
            int maxAssetCount = assetConcentration == 1 ? 1 : assetConcentration + 2;
            int lastChunkAssetIndex = len - 1;

            for (int i = 0; i < len && availableCellsNum > 0; i++) {
                SLTChunkAssetRule chunkAssetRule = randomChunkAssetRules.get(i);
                int count = i == lastChunkAssetIndex ? availableCells.size() : randomWithin(minAssetCount, maxAssetCount);
                generateAssetInstances(count, chunkAssetRule.getAssetId(), chunkAssetRule.getStateIds());
            }
        }
    }

    private void generateAssetInstances(int count, String assetId, List<String> stateIds) {
        SLTAsset asset = assetMap.get(assetId);

        for (int i = 0; i < count; i++) {
            int randCellIndex = (int) (Math.random() * availableCells.size());
            SLTCell randCell = availableCells.get(randCellIndex);
            randCell.setAssetInstance(layerToken, layerIndex, new SLTAssetInstance(asset.getToken(), asset.getInstanceStates(stateIds), asset.getProperties()));
            availableCells.subList(randCellIndex, randCellIndex + 1).clear();
            if (availableCells.isEmpty()) {
                return;
            }
        }
    }

    public List<SLTCell> cloneList(List<SLTCell> list) {
        List<SLTCell> clone = new ArrayList(list.size());
        for (SLTCell item : list) clone.add(item.clone());
        return clone;
    }
}
