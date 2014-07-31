/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

enum SLTDataType {
    LEVEL("level"),
    APP("app"),
    PLAYER_PROPERTY("playerProperty"),
    FEATURE("feature");

    private String type;

    SLTDataType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.type;
    }
}
