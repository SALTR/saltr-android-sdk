/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

public class SLTAsset {
    private Object properties;
    private String token;

    public SLTAsset(String token, Object properties) {
        this.token = token;
        this.properties = properties;
    }

    public Object getProperties() {
        return properties;
    }

    public String getToken() {
        return token;
    }

    public String toString() {
        return "[Asset] token: " + token + ", " + " properties: " + properties;
    }
}
