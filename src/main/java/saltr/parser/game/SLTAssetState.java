/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

public class SLTAssetState {
    private String token;
    private Object properties;

    public SLTAssetState(String token, Object properties) {
        this.token = token;
        this.properties = properties;
    }

    public String getToken() {
        return token;
    }

    public Object getProperties() {
        return properties;
    }
}
