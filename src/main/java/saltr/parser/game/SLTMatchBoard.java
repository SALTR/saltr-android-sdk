/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

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
