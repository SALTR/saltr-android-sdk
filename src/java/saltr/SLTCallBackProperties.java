/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.game.SLTLevel;
import saltr.game.SLTLevelPack;

//TODO:: @daal. What is this class about?
class SLTCallBackProperties {
    private SLTDataType dataType;
    private SLTLevelPack pack;
    private SLTLevel level;
    private String cachedFileName;

    SLTCallBackProperties(SLTDataType dataType) {
        this.dataType = dataType;
    }

    String getCachedFileName() {
        return cachedFileName;
    }

    void setCachedFileName(String cachedFileName) {
        this.cachedFileName = cachedFileName;
    }

    SLTDataType getDataType() {
        return dataType;
    }

    void setDataType(SLTDataType dataType) {
        this.dataType = dataType;
    }

    SLTLevelPack getPack() {
        return pack;
    }

    void setPack(SLTLevelPack pack) {
        this.pack = pack;
    }

    SLTLevel getLevel() {
        return level;
    }

    void setLevel(SLTLevel level) {
        this.level = level;
    }
}
