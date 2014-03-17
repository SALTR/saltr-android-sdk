/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

public class SLTAssetInstance {
    protected String state;
    protected Object keys;
    protected String type;

    public SLTAssetInstance(Object keys, String state, String type) {
        this.state = state;
        this.keys = keys;
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public Object getKeys() {
        return keys;
    }

    public String getType() {
        return type;
    }
}
