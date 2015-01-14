/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTAssetInstance;
import saltr.game.SLTAssetState;

import java.util.List;

/**
 * The SLT2DAssetInstance class represents the game 2D asset instance placed on board.
 */
public class SLT2DAssetInstance extends SLTAssetInstance {
    private Integer x;
    private Integer y;
    private Integer rotation;

    /**
     * @param token      The unique identifier of the asset.
     * @param states     The current instance states.
     * @param properties The current instance properties.
     * @param x          The current instance x coordinate.
     * @param y          The current instance y coordinate.
     * @param rotation   The current instance rotation.
     */
    public SLT2DAssetInstance(String token, List<SLTAssetState> states, Object properties, Integer x, Integer y, Integer rotation) {
        super(token, states, properties);

        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    /**
     * @return current instance x coordinate.
     */
    public Integer getX() {
        return x;
    }

    /**
     * @return current instance y coordinate.
     */
    public Integer getY() {
        return y;
    }

    /**
     * @return current instance rotation.
     */
    public Integer getRotation() {
        return rotation;
    }
}
