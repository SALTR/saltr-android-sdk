/*
 * Copyright (c) 2014 Plexonic Ltd
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
