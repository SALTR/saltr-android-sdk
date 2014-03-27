/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.response.level;

import java.util.Map;

public class SLTResponseLevelData {
    private Map<String, SLTResponseBoard> boards;
    private Map<String, Map<String, String>> keySets;
    private Map<String, String> assetStates;
    private Map<String, SLTResponseAsset> assets;
    private Map<String, Object> properties;

    public Map<String, SLTResponseBoard> getBoards() {
        return boards;
    }

    public void setBoards(Map<String, SLTResponseBoard> boards) {
        this.boards = boards;
    }

    public Map<String, Map<String, String>> getKeySets() {
        return keySets;
    }

    public void setKeySets(Map<String, Map<String, String>> keySets) {
        this.keySets = keySets;
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
