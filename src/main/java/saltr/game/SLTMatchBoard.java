/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

import java.util.List;
import java.util.Map;

public class SLTMatchBoard extends SLTBoard {
    private Integer rows;
    private Integer cols;
    private SLTCell[][] cells;

    public SLTMatchBoard(SLTCell[][] cells, List<SLTBoardLayer> layers, Map<String, String> properties) {
        super(layers, properties);
        this.cells = cells;
        cols = cells.length;
        rows = cells[0].length;
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getCols() {
        return cols;
    }

    public SLTCell[][] getCells() {
        return cells;
    }

    public void regenerateChunks() {
        for (SLTBoardLayer boardLayer : layers) {
            SLTMatchBoardLayer layer = (SLTMatchBoardLayer) boardLayer;
            layer.regenerateChunks();
        }
    }
}
