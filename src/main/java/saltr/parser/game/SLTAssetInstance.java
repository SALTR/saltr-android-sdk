/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

public class SLTAssetInstance {
    protected String state;
    protected Object properties;
    protected String token;

    public SLTAssetInstance(String token, String state, Object properties) {
        this.state = state;
        this.properties = properties;
        this.token = token;
    }

    public String getState() {
        return state;
    }

    public Object getProperties() {
        return properties;
    }

    public String getToken() {
        return token;
    }
}
