/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.response.level;

import java.util.Set;

public class SLTResponseBoardChunkAsset {
    private String assetId;
    private String distributionType;
    private Integer distributionValue;
    private String stateId;
    private Set<Long> states;

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

    public Integer getDistributionValue() {
        return distributionValue;
    }

    public void setDistributionValue(Integer distributionValue) {
        this.distributionValue = distributionValue;
    }

    public Set<Long> getStates() {
        return states;
    }

    public void setStates(Set<Long> states) {
        this.states = states;
    }
}
