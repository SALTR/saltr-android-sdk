package saltr;

/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
public enum SLTDataType {
    LEVEL("level"),
    APP("app"),
    FEATURE("feature");

    private String type;

    SLTDataType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.type;
    }
}
