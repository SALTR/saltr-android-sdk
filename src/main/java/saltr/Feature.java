/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import java.util.Map;

public class Feature {
    private String token;
    private Map<String, String> value;

    public Feature(String token, Map<String, String> data) {
        this.token = token;
        this.value = data;
    }

    public String getToken() {
        return token;
    }

    public Map<String, String> getValue() {
        return value;
    }
}
