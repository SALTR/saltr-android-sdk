/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response.level;

import java.io.Serializable;
import java.util.List;

public class SLTResponseBoardFixedAsset implements Serializable {
    private Long assetId;
    private Long stateId;
    private List<String> states;
    private int[][] cells;

    public int[][] getCells() {
        return cells;
    }

    public void setCells(int[][] cells) {
        this.cells = cells;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

}
