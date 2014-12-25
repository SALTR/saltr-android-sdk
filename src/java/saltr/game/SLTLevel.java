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

/**
 * Represents a level - a uniquely identifiable collection of boards and user defined properties.
 */
public class SLTLevel implements Comparable<SLTLevel> {
    /**
     * Used for parsing data retrieved from saltr.
     */
    public static final String LEVEL_TYPE_NONE = "noLevels";
    /**
     * A level with "matching" boards and assets.
     */
    public static final String LEVEL_TYPE_MATCHING = "matching";
    /**
     * A level with 2D boards and assets.
     */
    public static final String LEVEL_TYPE_2DCANVAS = "canvas2D";
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

    public static SLTLevelParser getParser(String levelType) {
        if (levelType.equals(LEVEL_TYPE_MATCHING)) {
            return SLTMatchingLevelParser.getInstance();
        }
        else if (levelType.equals(LEVEL_TYPE_2DCANVAS)) {
            return SLT2DLevelParser.getInstance();
        }
        return null;
    }

    /**
     * @return the index of the level in all levels.
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the properties of the level.
     */
    public Object getProperties() {
        return properties;
    }

    /**
     * @return the URL, used to retrieve contents of the level from Saltr.
     */
    public String getContentUrl() {
        return contentUrl;
    }

    /**
     * Gets a value indicating whether this {@link saltr.game.SLTLevel} content is ready to be read.
     *
     * @return <code>true</code> if content is ready; otherwise, <code>false</code>.
     */
    public Boolean getContentReady() {
        return contentReady;
    }

    /**
     * @return the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return the index of the level within its pack.
     */
    public int getLocalIndex() {
        return localIndex;
    }

    /**
     * @return the index of the pack the level is in.
     */
    public int getPackIndex() {
        return packIndex;
    }

    /**
     * Gets a board by id.
     * @param id Board identifier.
     * @return the board specified by the id.
     */
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

    /**
     * Regenerates contents of all boards.
     */
    public void regenerateAllBoards() {
        for (Map.Entry<String, SLTBoard> entry : boards.entrySet()) {
            entry.getValue().regenerate();
        }
    }

    /**
     * Regenerates contents of the board specified by boardId.
     * @param boardId Board identifier.
     */
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
