/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

import java.util.List;

public class SLTCompositeInstance extends SLTAssetInstance {
    private List<SLTCell> cells;

    public SLTCompositeInstance(Object keys, String state, String type, List<SLTCell> cells) {
        super(keys, state, type);
        this.cells = cells;
    }

    public List<SLTCell> getCells() {
        return cells;
    }
}
