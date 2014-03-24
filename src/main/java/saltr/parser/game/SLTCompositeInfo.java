/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;


import java.util.Map;

public class SLTCompositeInfo {
    private String assetId;
    private String stateId;
    private SLTCell cell;
    private Map<String, SLTAsset> assetMap;
    private Map<String, String> stateMap;

    public SLTCompositeInfo(String assetId, String stateId, SLTCell cell, SLTLevelSettings levelSettings) {
        this.assetId = assetId;
        this.stateId = stateId;
        this.cell = cell;
        this.assetMap = levelSettings.getAssetMap();
        this.stateMap = levelSettings.getStateMap();
    }

    public String getAssetId() {
        return assetId;
    }

    public void generate() {
        SLTCompositeAsset asset = (SLTCompositeAsset) assetMap.get(assetId);
        String state = stateMap.get(stateId);
        cell.setAssetInstance(new SLTCompositeInstance(asset.getKeys(), state, asset.getType(), asset.getCellInfos()));
    }
}
