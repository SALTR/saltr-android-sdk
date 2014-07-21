/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response.level;

import java.util.List;
import java.util.Map;

public class SLTResponseBoardProperties {
    private Map<String, String> board;
    private List<SLTResponseBoardPropertyCell> cell;

    public Map<String, String> getBoard() {
        return board;
    }

    public void setBoard(Map<String, String> board) {
        this.board = board;
    }

    public List<SLTResponseBoardPropertyCell> getCell() {
        return cell;
    }

    public void setCell(List<SLTResponseBoardPropertyCell> cell) {
        this.cell = cell;
    }
}
