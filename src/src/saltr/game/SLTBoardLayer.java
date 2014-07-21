/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

public abstract class SLTBoardLayer {
    private String token;
    private int index;

    public SLTBoardLayer(String token, int index) {
        this.token = token;
        this.index = index;
    }

    public String getToken() {
        return token;
    }

    public int getIndex() {
        return index;
    }

    public abstract void regenerate();
}
