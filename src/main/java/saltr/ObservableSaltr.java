package saltr;

import java.util.List;

/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
public interface ObservableSaltr {
    List<LevelPackStructure> getLevelPackStructures();

    void getLevelDataBody(LevelPackStructure levelPackData, LevelStructure levelData, Boolean useCache);

    public void addObserver(SaltrObserver o);

    public void removeObserver(SaltrObserver o);
}
