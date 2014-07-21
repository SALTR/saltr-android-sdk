/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response.level;

import java.util.List;

public class SLTResponseBoardChunkAsset {
    private String assetId;
    private String distributionType;
    private Integer distributionValue;
    private String stateId;
    private List<String> states;
    private Integer x;
    private Integer y;
    private Integer rotation;

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
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

    public Integer getDistributionValue() {
        return distributionValue;
    }

    public void setDistributionValue(Integer distributionValue) {
        this.distributionValue = distributionValue;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }
}
