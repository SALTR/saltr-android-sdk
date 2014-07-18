/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response.level;

import java.util.List;

public class SLTResponseBoard {
    private List<SLTResponseBoardLayer> layers;
    private Integer rows;
    private Integer cols;
    private List<List<Integer>> blockedCells;
    private Integer isFirstRowShifted;

    private Object cellSize;
    private Object orientation;
    private Object position;
    private SLTResponseBoardProperties properties;

    public List<SLTResponseBoardLayer> getLayers() {
        return layers;
    }

    public void setLayers(List<SLTResponseBoardLayer> layers) {
        this.layers = layers;
    }

    public Integer getIsFirstRowShifted() {
        return isFirstRowShifted;
    }

    public void setIsFirstRowShifted(Integer isFirstRowShifted) {
        this.isFirstRowShifted = isFirstRowShifted;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public List<List<Integer>> getBlockedCells() {
        return blockedCells;
    }

    public void setBlockedCells(List<List<Integer>> blockedCells) {
        this.blockedCells = blockedCells;
    }

    public Object getCellSize() {
        return cellSize;
    }

    public void setCellSize(Object cellSize) {
        this.cellSize = cellSize;
    }

    public Object getOrientation() {
        return orientation;
    }

    public void setOrientation(Object orientation) {
        this.orientation = orientation;
    }

    public Object getPosition() {
        return position;
    }

    public void setPosition(Object position) {
        this.position = position;
    }

    public SLTResponseBoardProperties getProperties() {
        return properties;
    }

    public void setProperties(SLTResponseBoardProperties properties) {
        this.properties = properties;
    }
}
