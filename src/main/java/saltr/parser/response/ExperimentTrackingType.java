package saltr.parser.response;

/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
public enum ExperimentTrackingType {
    NO_TRACK("no_track"),
    CUSTOM("custom"),
    ALL("all");

    private String track;

    ExperimentTrackingType(String track) {
        this.track = track;
    }

    public String getValue() {
        return this.track;
    }

    public static ExperimentTrackingType fromName(String track) {
        return valueOf(track.toUpperCase());
    }
}
