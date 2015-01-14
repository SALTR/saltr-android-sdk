/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

import java.util.List;

/**
 * The SLTLevelPack class represents the collection of levels.
 */
public class SLTLevelPack implements Comparable<SLTLevelPack> {
    private String token;
    private List<SLTLevel> levels;
    private int index;

    /**
     * @param token  The unique identifier of the level pack.
     * @param index  The index of the level pack.
     * @param levels The levels of the pack.
     */
    public SLTLevelPack(String token, int index, List<SLTLevel> levels) {
        this.token = token;
        this.index = index;
        this.levels = levels;
    }

    /**
     *
     * @return The unique identifier of the level pack.
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @return The levels of the pack.
     */
    public List<SLTLevel> getLevels() {
        return levels;
    }

    /**
     *
     * @return The index of the level pack.
     */
    public int getIndex() {
        return index;
    }

    /**
     *
     * @return the token.
     */
    public String toString() {
        return this.token;
    }

    @Override
    public int compareTo(SLTLevelPack o) {
        return (new Integer(this.index)).compareTo(new Integer(o.index));
    }
}
