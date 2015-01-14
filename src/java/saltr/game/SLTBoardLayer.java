/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

/**
 * The SLTBoardLayer class represents the game board's layer.
 */
public abstract class SLTBoardLayer {
    private String token;
    private int index;

    /**
     * @param token The unique identifier of the layer.
     * @param index The layer's ordering index.
     */
    public SLTBoardLayer(String token, int index) {
        this.token = token;
        this.index = index;
    }

    /**
     *
     * @return The unique identifier of the layer.
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @return The layer's ordering index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Regenerates the content of the layer.
     */
    public abstract void regenerate();
}
