/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTAssetInstance;
import saltr.game.SLTAssetState;

import java.util.List;

public class SLT2DAssetInstance extends SLTAssetInstance {
    private Integer x;
    private Integer y;
    private Integer rotation;

    public SLT2DAssetInstance(String token, List<SLTAssetState> states, Object properties, Integer x, Integer y, Integer rotation) {
        super(token, states, properties);

        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getRotation() {
        return rotation;
    }
}
