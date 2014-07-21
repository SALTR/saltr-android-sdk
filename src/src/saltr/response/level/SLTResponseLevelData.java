/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response.level;

import java.util.Map;

public class SLTResponseLevelData {
    private Map<String, SLTResponseBoard> boards;
    private Map<String, String> assetStates;
    private Map<String, SLTResponseAsset> assets;
    private Map<String, Object> properties;

    public Map<String, SLTResponseBoard> getBoards() {
        return boards;
    }

    public void setBoards(Map<String, SLTResponseBoard> boards) {
        this.boards = boards;
    }

    public Map<String, String> getAssetStates() {
        return assetStates;
    }

    public void setAssetStates(Map<String, String> assetStates) {
        this.assetStates = assetStates;
    }

    public Map<String, SLTResponseAsset> getAssets() {
        return assets;
    }

    public void setAssets(Map<String, SLTResponseAsset> assets) {
        this.assets = assets;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
