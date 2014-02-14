/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.gameeditor;

public class BoardAsset {
    protected String state;
    protected Object keys;
    protected String type;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Object getKeys() {
        return keys;
    }

    public void setKeys(Object keys) {
        this.keys = keys;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
