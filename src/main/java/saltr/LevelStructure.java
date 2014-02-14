/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import com.google.gson.Gson;
import saltr.parser.LevelParser;
import saltr.parser.data.Vector2D;
import saltr.parser.gameeditor.BoardData;
import saltr.parser.response.level.Board;
import saltr.parser.response.level.LevelData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelStructure implements Comparable<LevelStructure> {
    private String id;
    private String dataUrl;
    private int index;
    private Object properties;
    private Map<String, LevelBoard> boards;
    private Boolean dataFetched;
    private Object keyset;
    private String version;
    private LevelData data;

    public LevelStructure(String id, int index, String dataUrl, Object properties, String version) {
        this.id = id;
        this.index = index;
        this.dataUrl = dataUrl;
        this.dataFetched = false;
        this.properties = properties;
        this.version = version;
    }

    public void parseData(LevelData data) {
        this.data = data;
        BoardData boardData = LevelParser.parseBoardData(data);
        this.keyset = boardData.getKeyset();

        boards = new HashMap();
        LevelBoard levelBoard;
        Map<String, Board> boardsObject = data.getBoards();
        for (Map.Entry<String, Board> entry : boardsObject.entrySet()) {
            levelBoard = new LevelBoard(entry.getValue(), boardData);
            boards.put(entry.getKey(), levelBoard);
        }
        this.dataFetched = true;
    }

    public String getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public Object getKeyset() {
        return keyset;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public Boolean getDataFetched() {
        return dataFetched;
    }

    public String getVersion() {
        return version;
    }

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    public Map<String, Object> getInnerProperties() {
        return data.getProperties();
    }

    public LevelBoard getBoardById(String id) {
        return boards.get(id);
    }

    @Override
    public int compareTo(LevelStructure o) {
        return (new Integer(this.index)).compareTo(new Integer(o.index));
    }
}
