/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import saltr.game.SLTLevel;
import saltr.game.SLTLevelPack;

class SLTCallBackProperties {
    private SLTDataType dataType;
    private SLTLevelPack pack;
    private SLTLevel level;
    private String cachedFileName;

    public SLTCallBackProperties(SLTDataType dataType) {
        this.dataType = dataType;
    }

    public String getCachedFileName() {
        return cachedFileName;
    }

    public void setCachedFileName(String cachedFileName) {
        this.cachedFileName = cachedFileName;
    }

    public SLTDataType getDataType() {
        return dataType;
    }

    public void setDataType(SLTDataType dataType) {
        this.dataType = dataType;
    }

    public SLTLevelPack getPack() {
        return pack;
    }

    public void setPack(SLTLevelPack pack) {
        this.pack = pack;
    }

    public SLTLevel getLevel() {
        return level;
    }

    public void setLevel(SLTLevel level) {
        this.level = level;
    }
}
