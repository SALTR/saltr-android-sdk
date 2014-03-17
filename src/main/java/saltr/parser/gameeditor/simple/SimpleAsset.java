/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.gameeditor.simple;

import saltr.parser.game.SLTCell;
import saltr.parser.game.SLTAssetInstance;

public class SimpleAsset extends SLTAssetInstance {
    private SLTCell cell;

    public SLTCell getCell() {
        return cell;
    }

    public void setCell(SLTCell cell) {
        this.cell = cell;
    }

    public String toString() {
        return "Asset : [type : " + type + "]" + "[keys : " + keys + "]" + "[state : " + state + "]";
    }
}
