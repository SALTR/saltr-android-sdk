/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

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
