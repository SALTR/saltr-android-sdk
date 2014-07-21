/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTAssetState;

public class SLT2DAssetState extends SLTAssetState {

    private Integer pivotX;
    private Integer pivotY;

    public SLT2DAssetState(String token, Object properties, Integer pivotX, Integer pivotY) {
        super(token, properties);

        this.pivotX = pivotX;
        this.pivotY = pivotY;
    }

    public Integer getPivotX() {
        return pivotX;
    }

    public Integer getPivotY() {
        return pivotY;
    }
}
