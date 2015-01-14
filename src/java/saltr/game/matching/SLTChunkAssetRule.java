/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.matching;

import java.util.List;

/**
 * The SLTChunkAssetRule class represents the asset's rule.
 */
public class SLTChunkAssetRule {
    private String assetId;
    private List<String> stateIds;
    private String distributionType;
    private Float distributionValue;

    /**
     * @param assetId           The asset identifier.
     * @param distributionType  The distribution type.
     * @param distributionValue The distribution value.
     * @param stateIds          The state identifiers.
     */
    public SLTChunkAssetRule(String assetId, String distributionType, Float distributionValue, List<String> stateIds) {
        this.assetId = assetId;
        this.stateIds = stateIds;
        this.distributionType = distributionType;
        this.distributionValue = distributionValue;
    }

    /**
     *
     * @return The asset identifier.
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     *
     * @return The The state identifiers.
     */
    public List<String> getStateIds() {
        return stateIds;
    }

    /**
     *
     * @return The distribution type.
     */
    public String getDistributionType() {
        return distributionType;
    }

    /**
     *
     * @return The distribution value.
     */
    public Float getDistributionValue() {
        return distributionValue;
    }
}
