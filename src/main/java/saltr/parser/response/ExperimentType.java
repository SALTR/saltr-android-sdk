package saltr.parser.response;

/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
public enum ExperimentType {
    FEATURE("feature"),
    PACK("pack");

    private String type;

    ExperimentType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.type;
    }

    public static ExperimentType fromName(String type) {
        if (type != null) {
            return valueOf(type.toUpperCase());
        }
        return null;
    }
}
