/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.response.level;

import java.util.Map;

public class LevelData {
    private Map<String, Board> boards;
    private Map<String, Map<String, String>> keySets;
    private Map<String, String> assetStates;
    private Map<String, SaltrAsset> assets;
    private Map<String, Object> properties;

    public Map<String, Board> getBoards() {
        return boards;
    }

    public void setBoards(Map<String, Board> boards) {
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

    public Map<String, SaltrAsset> getAssets() {
        return assets;
    }

    public void setAssets(Map<String, SaltrAsset> assets) {
        this.assets = assets;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
