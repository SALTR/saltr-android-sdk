/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.gameeditor.composite;

import saltr.parser.data.Vector2D;
import saltr.parser.gameeditor.BoardData;
import saltr.parser.gameeditor.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Composite {
    private String id;
    private Cell position;
    private Vector2D outputBoard;
    private Map boardAssetMap;

    public Composite(String id, Cell position, Vector2D outputBoard, BoardData boardData) {
        this.id = id;
        this.position = position;
        this.outputBoard = outputBoard;
        this.boardAssetMap = boardData.getAssetMap();
    }

    public String getId() {
        return id;
    }

    public Cell getPosition() {
        return position;
    }

    public void generate() {
        CompositeAssetTemplate compositeAssetTemplate = (CompositeAssetTemplate) boardAssetMap.get(id);
        CompositeAsset compositeAsset = new CompositeAsset();
        compositeAsset.setKeys(compositeAssetTemplate.getKeys());
        compositeAsset.setType(compositeAssetTemplate.getType());
        List cellShifts = compositeAssetTemplate.getShifts();
        List<Integer> shift;
        List<Cell> shifts = new ArrayList<Cell>();
        for (int i = 0, len = cellShifts.size(); i < len; ++i) {
            shift = (List<Integer>) cellShifts.get(i);
            shifts.add(new Cell(shift.get(0), shift.get(1)));
        }
        compositeAsset.setShifts(shifts);
        compositeAsset.setBasis(new Cell(position.getX(), position.getY()));
        outputBoard.insert(position.getX(), position.getY(), compositeAsset);
    }
}
