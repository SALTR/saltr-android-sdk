/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.matching;

import saltr.game.SLTAssetInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a matching board cell.
 */
public class SLTCell implements Cloneable {
    private int row;
    private int col;
    private Map<String, Object> properties;
    private Boolean isBlocked;
    private Map<String, SLTAssetInstance> instancesByLayerId;
    private Map<String, SLTAssetInstance> instancesByLayerIndex;

    public SLTCell(int row, int col) {
        this.row = row;
        this.col = col;
        properties = new HashMap<String, Object>();
        isBlocked = false;
        instancesByLayerId = new HashMap<String, SLTAssetInstance>();
        instancesByLayerIndex = new HashMap<String, SLTAssetInstance>();
    }

    /**
     * @return the row of the cell.
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row to set.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the column of the cell.
     */
    public int getCol() {
        return col;
    }

    /**
     * @param col to set.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * @return a value indicating whether this {@link saltr.game.matching.SLTCell} is blocked.
     */
    public Boolean getIsBlocked() {
        return isBlocked;
    }

    /**
     * Sets a value indicating whether this {@link saltr.game.matching.SLTCell} is blocked.
     *
     * @param isBlocked <code>true</code> if is blocked; otherwise, <code>false</code>.
     */
    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    /**
     * @return the properties of the cell.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * @param properties  of the cell to set.
     */
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * Gets the asset instance by layer identifier(token).
     * @param layerId Layer identifier(token).
     * @return The asset instance that is positioned in the cell in the layer specified by layerId.
     */
    public SLTAssetInstance getAssetInstanceByLayerId(String layerId) {
        return instancesByLayerId.get(layerId);
    }

    /**
     * Gets the asset instance by layer index.
     * @param layerIndex Layer index.
     * @return the asset instance that is positioned in the cell in the layer specified by layerIndex.
     */
    public SLTAssetInstance getAssetInstanceByLayerIndex(int layerIndex) {
        return instancesByLayerIndex.get(layerIndex);
    }

    public void setInstancesByLayerId(Map<String, SLTAssetInstance> instancesByLayerId) {
        this.instancesByLayerId = instancesByLayerId;
    }

    public void setInstancesByLayerIndex(Map<String, SLTAssetInstance> instancesByLayerIndex) {
        this.instancesByLayerIndex = instancesByLayerIndex;
    }

    public void setAssetInstance(String layerId, int layerIndex, SLTAssetInstance assetInstance) {
        if (!isBlocked) {
            instancesByLayerId.put(layerId, assetInstance);
            instancesByLayerIndex.put(String.valueOf(layerIndex), assetInstance);
        }
    }

    public void removeAssetInstance(String layerId, int layerIndex) {
        instancesByLayerId.remove(layerId);
        instancesByLayerIndex.remove(String.valueOf(layerIndex));
    }

    @Override
    public SLTCell clone() {
        SLTCell cell = new SLTCell(row, col);
        cell.setProperties(properties);
        cell.setIsBlocked(isBlocked);
        cell.setInstancesByLayerId(instancesByLayerId);
        cell.setInstancesByLayerIndex(instancesByLayerIndex);
        return cell;
    }
}
