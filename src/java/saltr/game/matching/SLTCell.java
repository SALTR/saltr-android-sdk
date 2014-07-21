/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.matching;

import saltr.game.SLTAssetInstance;

import java.util.HashMap;
import java.util.Map;

public class SLTCell implements Cloneable {
    private int row;
    private int col;
    private Map<String, String> properties;
    private Boolean isBlocked;
    private Map<String, SLTAssetInstance> instancesByLayerId;
    private Map<String, SLTAssetInstance> instancesByLayerIndex;

    public SLTCell(int row, int col) {
        this.row = row;
        this.col = col;
        properties = new HashMap<>();
        isBlocked = false;
        instancesByLayerId = new HashMap<>();
        instancesByLayerIndex = new HashMap<>();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public SLTAssetInstance getAssetInstanceByLayerId(String layerId) {
        return instancesByLayerId.get(layerId);
    }

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
