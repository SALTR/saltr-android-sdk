/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.gameeditor.composite;

import saltr.parser.gameeditor.BoardAsset;
import saltr.parser.gameeditor.Cell;

import java.util.List;

public class CompositeAsset extends BoardAsset {
    private List<Cell> shifts;
    private Cell basis;

    public List<Cell> getShifts() {
        return shifts;
    }

    public void setShifts(List<Cell> shifts) {
        this.shifts = shifts;
    }

    public Cell getBasis() {
        return basis;
    }

    public void setBasis(Cell basis) {
        this.basis = basis;
    }
}
