/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.response.level;

import java.util.List;
import java.util.Map;

public class CustomProperties {
    private Map<String, String> board;
    private List<CustomPropertyCell> cell;

    public Map<String, String> getBoard() {
        return board;
    }

    public void setBoard(Map<String, String> board) {
        this.board = board;
    }

    public List<CustomPropertyCell> getCell() {
        return cell;
    }

    public void setCell(List<CustomPropertyCell> cell) {
        this.cell = cell;
    }
}
