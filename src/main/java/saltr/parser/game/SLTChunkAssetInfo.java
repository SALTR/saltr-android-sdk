/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

public class SLTChunkAssetInfo {
    private String assetId;
    private int count;
    private String stateId;

    public SLTChunkAssetInfo(String assetId, int count, String stateId) {
        this.assetId = assetId;
        this.count = count;
        this.stateId = stateId;
    }

    public String getAssetId() {
        return assetId;
    }

    public int getCount() {
        return count;
    }

    public String getStateId() {
        return stateId;
    }
}
