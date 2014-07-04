/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

import java.util.List;

public class SLTLevelPack implements Comparable<SLTLevelPack> {
    private String token;
    private List<SLTLevel> levels;
    private int index;

    public SLTLevelPack(String token, int index, List<SLTLevel> levels) {
        this.token = token;
        this.index = index;
        this.levels = levels;
    }

    public String getToken() {
        return token;
    }

    public List<SLTLevel> getLevels() {
        return levels;
    }

    public int getIndex() {
        return index;
    }

    public String toString() {
        return this.token;
    }

    @Override
    public int compareTo(SLTLevelPack o) {
        return (new Integer(this.index)).compareTo(new Integer(o.index));
    }
}
