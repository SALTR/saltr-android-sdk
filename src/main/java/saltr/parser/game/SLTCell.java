/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

import java.util.Map;

public class SLTCell implements Cloneable {
    private int row;
    private int col;
    private Map<String, String> properties;
    private Boolean isBlocked;
    private Map<Object, SLTAssetInstance> instancesByLayerId;
    private Map<Object, SLTAssetInstance> instancesByLayerIndex;

    public SLTCell(int row, int col) {
        this.row = row;
        this.col = col;
        isBlocked = false;
    }

    public Map<Object, SLTAssetInstance> getInstancesByLayerId() {
        return instancesByLayerId;
    }

    public void setInstancesByLayerId(Map<Object, SLTAssetInstance> instancesByLayerId) {
        this.instancesByLayerId = instancesByLayerId;
    }

    public Map<Object, SLTAssetInstance> getInstancesByLayerIndex() {
        return instancesByLayerIndex;
    }

    public void setInstancesByLayerIndex(Map<Object, SLTAssetInstance> instancesByLayerIndex) {
        this.instancesByLayerIndex = instancesByLayerIndex;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
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

    public void setAssetInstance(String layerId, int layerIndex, SLTAssetInstance assetInstance) {
        if (!isBlocked) {
            instancesByLayerId.put(layerId, assetInstance);
            instancesByLayerId.put(layerIndex, assetInstance);
        }
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
