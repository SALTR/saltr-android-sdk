/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

import saltr.parser.game.SLTAsset;

import java.util.List;

public class SLTCompositeAsset extends SLTAsset {
    private List cellInfos;

    public SLTCompositeAsset(List cellInfos, String type, Object keys) {
        super(type, keys);
        this.cellInfos = cellInfos;
    }

    public List getCellInfos() {
        return cellInfos;
    }
}
