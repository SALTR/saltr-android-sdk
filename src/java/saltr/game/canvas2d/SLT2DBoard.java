/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTBoard;
import saltr.game.SLTBoardLayer;

import java.util.List;
import java.util.Map;

public class SLT2DBoard extends SLTBoard {

    private Integer width;
    private Integer height;

    public SLT2DBoard(Integer width, Integer height, List<SLTBoardLayer> layers, Map<String, Object> properties) {
        super(layers, properties);
        this.width = width;
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
