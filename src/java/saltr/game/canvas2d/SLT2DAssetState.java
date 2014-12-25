/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTAssetState;

/**
 * Represents a state of a 2D asset
 */
public class SLT2DAssetState extends SLTAssetState {

    private Integer pivotX;
    private Integer pivotY;

    public SLT2DAssetState(String token, Object properties, Integer pivotX, Integer pivotY) {
        super(token, properties);

        this.pivotX = pivotX;
        this.pivotY = pivotY;
    }

    /**
     * @return the X coordinate of the pivot relative to the top left corner, in pixels.
     */
    public Integer getPivotX() {
        return pivotX;
    }


    /**
     * @return the Y coordinate of the pivot relative to the top left corner, in pixels.
     */
    public Integer getPivotY() {
        return pivotY;
    }
}
