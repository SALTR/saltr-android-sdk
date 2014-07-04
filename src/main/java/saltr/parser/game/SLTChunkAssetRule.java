/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

public class SLTChunkAssetRule {
    private String assetId;
    private String stateId;
    private String distributionType;
    private int distributionValue;

    public SLTChunkAssetRule(String assetId, String stateId, String distributionType, int distributionValue) {
        this.assetId = assetId;
        this.stateId = stateId;
        this.distributionType = distributionType;
        this.distributionValue = distributionValue;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(String distributionType) {
        this.distributionType = distributionType;
    }

    public int getDistributionValue() {
        return distributionValue;
    }

    public void setDistributionValue(int distributionValue) {
        this.distributionValue = distributionValue;
    }
}
