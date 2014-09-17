/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response.level;

import java.util.List;
import java.util.Map;

public class SLTResponseBoard {
    private List<SLTResponseBoardLayer> layers;
    private Integer rows;
    private Integer cols;
    private List<List<Integer>> blockedCells;
    private Integer isFirstRowShifted;
    private String orientation;
    private Integer width;
    private Integer height;
    private Map<String, Object> properties;
    private List<SLTResponseBoardPropertyCell> cellProperties;

    public List<SLTResponseBoardPropertyCell> getCellProperties() {
        return cellProperties;
    }

    public void setCellProperties(List<SLTResponseBoardPropertyCell> cellProperties) {
        this.cellProperties = cellProperties;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

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

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}
