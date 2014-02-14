/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.gameeditor.composite;

import saltr.parser.gameeditor.simple.SimpleAssetTemplate;

import java.util.List;

public class CompositeAssetTemplate extends SimpleAssetTemplate {
    private List shifts;

    public CompositeAssetTemplate(List shifts, String typeKey, Object keys) {
        super(typeKey, keys);
        this.shifts = shifts;
    }

    public List getShifts() {
        return shifts;
    }
}
