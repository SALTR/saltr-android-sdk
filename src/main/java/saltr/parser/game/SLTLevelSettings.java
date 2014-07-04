/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;


import java.util.Map;

public class SLTLevelSettings {
    private Map<String, SLTAsset> assetMap;
    private Map<String, String> stateMap;

    public SLTLevelSettings(Map<String, SLTAsset> assetMap, Map<String, String> stateMap) {
        this.assetMap = assetMap;
        this.stateMap = stateMap;
    }

    public Map<String, SLTAsset> getAssetMap() {
        return assetMap;
    }

    public Map<String, String> getStateMap() {
        return stateMap;
    }
}
