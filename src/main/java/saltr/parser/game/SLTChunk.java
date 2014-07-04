/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;


import java.util.*;

public class SLTChunk {
    private SLTLevelBoardLayer layer;
    private List<SLTChunkAssetRule> chunkAssetRules;
    private List<SLTCell> chunkCells;
    private List<SLTCell> availableCells;
    private Map<String, SLTAsset> assetMap;
    private Map<String, String> stateMap;

    public SLTChunk(SLTLevelBoardLayer layer, List<SLTCell> chunkCells, List<SLTChunkAssetRule> chunkAssetRules, SLTLevelSettings levelSettings) {
        this.layer = layer;
        this.chunkCells = chunkCells;
        this.chunkAssetRules = chunkAssetRules;

        this.availableCells = new ArrayList<>();
        this.assetMap = levelSettings.getAssetMap();
        this.stateMap = levelSettings.getStateMap();
    }

    public String toString() {
        return "[Chunk] cells:" + availableCells.size() + ", " + " chunkAssets: " + chunkAssetRules.size();
    }

    private void generateCellContent() {
        availableCells = cloneList(chunkCells);
        List<SLTChunkAssetRule> countChunkAssetRules = new ArrayList<>();
        List<SLTChunkAssetRule> ratioChunkAssetRules = new ArrayList<>();
        List<SLTChunkAssetRule> randomChunkAssetRules = new ArrayList<>();

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

    private void generateAssetInstances(int count, String assetId, String stateId) {
        SLTAsset asset = assetMap.get(assetId);
        String state = stateMap.get(assetId);

        int randCellIndex;
        SLTCell randCell;

        for (int i = 0; i < count; ++i) {
            randCellIndex = (int) (Math.random() * availableCells.size());
            randCell = availableCells.get(randCellIndex);
            randCell.setAssetInstance(layer.getLayerId(), layer.getLayerIndex(), new SLTAssetInstance(asset.getToken(), state, asset.getProperties()));
            availableCells.subList(randCellIndex, randCellIndex + 1).clear();
            if (availableCells.isEmpty()) {
                return;
            }
        }
    }

    private void generateAssetInstancesByCount(List<SLTChunkAssetRule> countChunkAssetRules) {
        for (int i = 0, len = countChunkAssetRules.size(); i < len; ++i) {
            SLTChunkAssetRule assetRule = countChunkAssetRules.get(i);
            generateAssetInstances(assetRule.getDistributionValue(), assetRule.getAssetId(), assetRule.getStateId());
        }
    }

    private void generateAssetInstancesByRatio(List<SLTChunkAssetRule> ratioChunkAssetRules) {
        int ratioSum = 0;
        SLTChunkAssetRule assetRule;
        for (int i = 0; i < ratioChunkAssetRules.size(); ++i) {
            assetRule = ratioChunkAssetRules.get(i);
            ratioSum += assetRule.getDistributionValue();
        }
        int availableCellsNum = availableCells.size();
        Integer proportion;
        int count;

        List<Map<String, Object>> fractionAssets = new ArrayList();
        if (ratioSum != 0) {
            for (int j = 0; j < ratioChunkAssetRules.size(); ++j) {
                assetRule = ratioChunkAssetRules.get(j);
                proportion = assetRule.getDistributionValue() / ratioSum * availableCellsNum;
                count = proportion; //assigning number to int to floor the value;
                Map<String, Object> fractionAsset = new HashMap<>();
                fractionAsset.put("fraction", proportion - count);
                fractionAsset.put("assetRule", assetRule);
                fractionAssets.add(fractionAsset);
                generateAssetInstances(count, assetRule.getAssetId(), assetRule.getStateId());
            }

            Comparator<Map<String, Object>> comparator = new Comparator<Map<String, Object>>() {
                public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                    Integer i1 = Integer.valueOf(m1.get("fraction").toString());
                    Integer i2 = Integer.valueOf(m2.get("fraction").toString());
                    return i2.compareTo(i1);
                }
            };

            Collections.sort(fractionAssets, comparator);

            for (int k = 0; k < availableCells.size(); ++k) {
                SLTChunkAssetRule rule = (SLTChunkAssetRule) fractionAssets.get(k).get("assetRule");
                generateAssetInstances(1, rule.getAssetId(), rule.getStateId());
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

            SLTChunkAssetRule chunkAssetRule;
            int count;
            for (int i = 0; i < len && availableCellsNum > 0; ++i) {
                chunkAssetRule = randomChunkAssetRules.get(i);
                count = i == lastChunkAssetIndex ? availableCells.size() : randomWithin(minAssetCount, maxAssetCount);
                generateAssetInstances(count, chunkAssetRule.getAssetId(), chunkAssetRule.getStateId());
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
