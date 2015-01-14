/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTAssetState;

/**
 * The SLT2DAssetState class represents the 2D asset state and provides the state related properties.
 */
public class SLT2DAssetState extends SLTAssetState {

    private Integer pivotX;
    private Integer pivotY;

    /**
     * @param token      The unique identifier of the state.
     * @param properties The current state related properties.
     * @param pivotX     The X coordinate of the pivot relative to the top left corner, in pixels.
     * @param pivotY     The Y coordinate of the pivot relative to the top left corner, in pixels.
     */
    public SLT2DAssetState(String token, Object properties, Integer pivotX, Integer pivotY) {
        super(token, properties);

        this.pivotX = pivotX;
        this.pivotY = pivotY;
    }

    /**
     * @return The X coordinate of the pivot relative to the top left corner, in pixels.
     */
    public Integer getPivotX() {
        return pivotX;
    }


    /**
     * @return The Y coordinate of the pivot relative to the top left corner, in pixels.
     */
    public Integer getPivotY() {
        return pivotY;
    }
}
