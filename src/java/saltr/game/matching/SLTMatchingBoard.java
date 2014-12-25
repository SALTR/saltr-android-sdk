/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.matching;

import saltr.game.SLTBoard;
import saltr.game.SLTBoardLayer;

import java.util.List;
import java.util.Map;

/**
 * Represents a matching board.
 */
public class SLTMatchingBoard extends SLTBoard {
    private Integer rows;
    private Integer cols;
    private SLTCell[][] cells;

    public SLTMatchingBoard(SLTCell[][] cells, List<SLTBoardLayer> layers, Map<String, Object> properties) {
        super(layers, properties);
        this.cells = cells;
        cols = cells.length;
        rows = cells[0].length;
    }

    /**
     * @return the number of rows.
     */
    public Integer getRows() {
        return rows;
    }

    /**
     * @return the number of columns.
     */
    public Integer getCols() {
        return cols;
    }

    /**
     * @return the cells.
     */
    public SLTCell[][] getCells() {
        return cells;
    }

    public void regenerateChunks() {
        for (SLTBoardLayer boardLayer : layers) {
            SLTMatchingBoardLayer layer = (SLTMatchingBoardLayer) boardLayer;
            layer.regenerate();
        }
    }
}
