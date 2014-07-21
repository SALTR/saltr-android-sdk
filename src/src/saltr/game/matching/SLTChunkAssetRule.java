/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.matching;

import java.util.List;

public class SLTChunkAssetRule {
    private String assetId;
    private List<String> stateIds;
    private String distributionType;
    private int distributionValue;

    public SLTChunkAssetRule(String assetId, String distributionType, int distributionValue, List<String> stateIds) {
        this.assetId = assetId;
        this.stateIds = stateIds;
        this.distributionType = distributionType;
        this.distributionValue = distributionValue;
    }

    public String getAssetId() {
        return assetId;
    }

    public List<String> getStateIds() {
        return stateIds;
    }

    public String getDistributionType() {
        return distributionType;
    }

    public int getDistributionValue() {
        return distributionValue;
    }
}
