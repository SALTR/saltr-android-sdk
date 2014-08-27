/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response.level;

import java.util.List;
import java.util.Map;

public class SLTResponseBoardProperties {
    private Map<String, Object> board;
    private List<SLTResponseBoardPropertyCell> cell;

    public Map<String, Object> getBoard() {
        return board;
    }

    public void setBoard(Map<String, Object> board) {
        this.board = board;
    }

    public List<SLTResponseBoardPropertyCell> getCell() {
        return cell;
    }

    public void setCell(List<SLTResponseBoardPropertyCell> cell) {
        this.cell = cell;
    }
}
