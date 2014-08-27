/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

import saltr.exception.SLTException;
import saltr.exception.SLTLevelParserNullPointerException;
import saltr.game.canvas2d.SLT2DLevelParser;
import saltr.game.matching.SLTMatchingLevelParser;
import saltr.response.level.SLTResponseBoard;
import saltr.response.level.SLTResponseLevelContentData;

import java.util.Map;

public class SLTLevel implements Comparable<SLTLevel> {
    protected Map<String, SLTBoard> boards;

    private String id;
    private String levelType;
    private int index;
    private int localIndex;
    private int packIndex;
    private String contentUrl;
    private Map<String, Object> properties;
    private String version;

    private Boolean contentReady;
    private Map<String, SLTAsset> assetMap;

    public static final String LEVEL_TYPE_NONE = "noLevels";
    public static final String LEVEL_TYPE_MATCHING = "matching";
    public static final String LEVEL_TYPE_2DCANVAS = "canvas2D";


    public static SLTLevelParser getParser(String levelType) {
        if (levelType.equals(LEVEL_TYPE_MATCHING)) {
            return SLTMatchingLevelParser.getInstance();
        }
        else if (levelType.equals(LEVEL_TYPE_2DCANVAS)) {
            return SLT2DLevelParser.getInstance();
        }
        return null;
    }


    public SLTLevel(String id, String levelType, int index, int localIndex, int packIndex, String contentUrl, Map<String, Object> properties, String version) {
        this.id = id;
        this.levelType = levelType;
        this.index = index;
        this.localIndex = localIndex;
        this.packIndex = packIndex;
        this.contentUrl = contentUrl;
        this.properties = properties;
        this.version = version;
        this.contentReady = false;
    }

    public int getIndex() {
        return index;
    }

    public Object getProperties() {
        return properties;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public Boolean getContentReady() {
        return contentReady;
    }

    public String getVersion() {
        return version;
    }

    public int getLocalIndex() {
        return localIndex;
    }

    public int getPackIndex() {
        return packIndex;
    }

    public SLTBoard getBoard(String id) {
        return boards.get(id);
    }

    public void updateContent(SLTResponseLevelContentData rootNode) throws SLTException {
        Map<String, SLTResponseBoard> boardsNode;
        if (rootNode.getBoards() != null) {
            boardsNode = rootNode.getBoards();
        }
        else {
            throw new SLTException("[SALTR: ERROR] Level content's 'boards' node can not be found.");
        }

        properties = rootNode.getProperties();

        SLTLevelParser parser = getParser(levelType);
        if (parser != null) {
            try {
                assetMap = parser.parseLevelAssets(rootNode);
            } catch (Exception e) {
                throw new SLTException("[SALTR: ERROR] Level content boards parsing failed.");
            }

            try {
                boards = parser.parseLevelContent(boardsNode, assetMap);
            } catch (Exception e) {
                throw new SLTException("[SALTR: ERROR] Level content boards parsing failed.");
            }

            if (boards != null) {
                regenerateAllBoards();
                contentReady = true;
            }
        }
        else {
            throw new SLTLevelParserNullPointerException();
        }
    }

    public void regenerateAllBoards() {
        for (Map.Entry<String, SLTBoard> entry : boards.entrySet()) {
            entry.getValue().regenerate();
        }
    }

    public void regenerateBoard(String boardId) {
        if (boards != null && boards.get(boardId) != null) {
            SLTBoard board = boards.get(boardId);
            board.regenerate();
        }
    }

    @Override
    public int compareTo(SLTLevel o) {
        return (new Integer(this.index)).compareTo(o.index);
    }
}
