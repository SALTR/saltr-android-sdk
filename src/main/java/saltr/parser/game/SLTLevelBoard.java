/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

import java.util.Map;

public class SLTLevelBoard {
    private Integer rows;
    private Integer cols;
    private SLTCell[][] cells;
    private Map<String, String> properties;
    private Map<String, Integer> layers;

    public SLTLevelBoard(SLTCell[][] cells, Map<String, Integer> layers, Map<String, String> properties) {
        this.cells = cells;
        cols = cells.length;
        rows = cells[0].length;
        this.layers = layers;
        this.properties = properties;
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

    public Map<String, String> getProperties() {
        return properties;
    }

    public Map<String, Integer> getLayers() {
        return layers;
    }
}
