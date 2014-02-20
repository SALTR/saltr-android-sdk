/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

class CallBackDetails {
    private DataType dataType;
    private LevelPackStructure levelPackData;
    private LevelStructure levelData;
    private String cachedFileName;

    public CallBackDetails(DataType dataType) {
        this.dataType = dataType;
    }

    public String getCachedFileName() {
        return cachedFileName;
    }

    public void setCachedFileName(String cachedFileName) {
        this.cachedFileName = cachedFileName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public LevelPackStructure getLevelPackData() {
        return levelPackData;
    }

    public void setLevelPackData(LevelPackStructure levelPackData) {
        this.levelPackData = levelPackData;
    }

    public LevelStructure getLevelData() {
        return levelData;
    }

    public void setLevelData(LevelStructure levelData) {
        this.levelData = levelData;
    }
}
