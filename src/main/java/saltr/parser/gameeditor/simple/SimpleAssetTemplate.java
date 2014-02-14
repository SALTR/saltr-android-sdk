/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.gameeditor.simple;

import java.util.List;

public class SimpleAssetTemplate implements IAssetTemplate {
    private Object keys;
    private String type;

    public SimpleAssetTemplate(String typeKey, Object keys) {
        this.type = typeKey;
        this.keys = keys;
    }

    public Object getKeys() {
        return keys;
    }

    public String getType() {
        return type;
    }

    @Override
    public List getShifts() {
        return null;
    }

    public String toString() {
        return "AssetTemplate : [type : " + type + "]" + "[keys : " + keys.toString() + "]";
    }
}
