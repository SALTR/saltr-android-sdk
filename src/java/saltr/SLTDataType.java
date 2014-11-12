/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

enum SLTDataType {
    LEVEL("level"),
    APP("app"),
    PLAYER_PROPERTY("playerProperty"),
    ADD_DEVICE("addDevice"),
    CLIENT_DATA("clientData");

    private String type;

    SLTDataType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.type;
    }
}
