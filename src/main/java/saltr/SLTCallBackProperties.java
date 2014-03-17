/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import saltr.parser.game.SLTLevel;
import saltr.parser.game.SLTLevelPack;

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
