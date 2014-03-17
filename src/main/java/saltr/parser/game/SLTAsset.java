/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

public class SLTAsset {
    private Object keys;
    private String type;

    public SLTAsset(String type, Object keys) {
        this.type = type;
        this.keys = keys;
    }

    public Object getKeys() {
        return keys;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return "[Asset] type: " + type + ", " + " keys: " + keys;
    }
}
