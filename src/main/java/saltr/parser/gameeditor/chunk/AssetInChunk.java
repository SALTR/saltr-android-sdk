/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.gameeditor.chunk;

public class AssetInChunk {
    private String id;
    private int count;
    private String stateId;

    public AssetInChunk(String id, int count, String stateId) {
        this.id = id;
        this.count = count;
        this.stateId = stateId;
    }

    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public String getStateId() {
        return stateId;
    }
}
