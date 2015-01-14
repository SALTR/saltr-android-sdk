/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTBoard;
import saltr.game.SLTBoardLayer;

import java.util.List;
import java.util.Map;

/**
 * The SLT2DBoard class represents the 2D game board.
 */
public class SLT2DBoard extends SLTBoard {

    private Integer width;
    private Integer height;

    /**
     * @param width      The width of the board in pixels as is in Saltr level editor.
     * @param height     The height of the board in pixels as is in Saltr level editor.
     * @param layers     The layers of the board.
     * @param properties The board associated properties.
     */
    public SLT2DBoard(Integer width, Integer height, List<SLTBoardLayer> layers, Map<String, Object> properties) {
        super(layers, properties);
        this.width = width;
        this.height = height;
    }

    /**
     * @return The width of the board in pixels as is in Saltr level editor.
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @return The height of the board in pixels as is in Saltr level editor.
     */
    public Integer getHeight() {
        return height;
    }
}
