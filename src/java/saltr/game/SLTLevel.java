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
 * The SLTLevel class represents the game's level.
 */
public class SLTLevel implements Comparable<SLTLevel> {
    /**
     * Specifies that there is no level specified for the game.
     */
    public static final String LEVEL_TYPE_NONE = "noLevels";
    /**
     * Specifies the level type for matching game.
     */
    public static final String LEVEL_TYPE_MATCHING = "matching";
    /**
     * Specifies the level type for Canvas2D game.
     */
    public static final String LEVEL_TYPE_2DCANVAS = "canvas2D";
    protected Map<String, SLTBoard> boards;
    private String id;
    private String variationId;
    private String levelType;
    private int index;
    private int localIndex;
    private int packIndex;
    private String contentUrl;
    private Map<String, Object> properties;
    private String version;
    private Boolean contentReady;
    private Map<String, SLTAsset> assetMap;

    /**
     * @param id          The identifier of the level.
     * @param variationId The variation identifier of the level.
     * @param levelType   The type of the level.
     * @param index       The global index of the level.
     * @param localIndex  The local index of the level in the pack.
     * @param packIndex   The index of the pack the level is in.
     * @param contentUrl  The content URL of the level.
     * @param properties  The properties of the level.
     * @param version     The current version of the level.
     */
    public SLTLevel(String id, String variationId, String levelType, int index, int localIndex, int packIndex, String contentUrl, Map<String, Object> properties, String version) {
        this.id = id;
        this.variationId = variationId;
        this.levelType = levelType;
        this.index = index;
        this.localIndex = localIndex;
        this.packIndex = packIndex;
        this.contentUrl = contentUrl;
        this.properties = properties;
        this.version = version;
        this.contentReady = false;
    }

    /**
     * Provides the level parser for the given level type.
     * @param levelType The type of the level.
     * @return The level type corresponding level parser.
     */
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
     *
     * @return The variation identifier of the level.
     */
    public String getVariationId() {
        return variationId;
    }

    /**
     * @return The global index of the level.
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return The properties of the level.
     */
    public Object getProperties() {
        return properties;
    }

    /**
     * @return The content URL of the level.
     */
    public String getContentUrl() {
        return contentUrl;
    }

    /**
     * @return The content ready state.
     */
    public Boolean getContentReady() {
        return contentReady;
    }

    /**
     * @return The current version of the level.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return The local index of the level in the pack.
     */
    public int getLocalIndex() {
        return localIndex;
    }

    /**
     * @return The index of the pack the level is in.
     */
    public int getPackIndex() {
        return packIndex;
    }

    /**
     * Gets the board by identifier.
     * @param id The board identifier.
     * @return The board with provided identifier.
     */
    public SLTBoard getBoard(String id) {
        return boards.get(id);
    }

    /**
     * Updates the content of the level.
     */
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
     * Regenerates content of the board by identifier.
     * @param boardId The board identifier.
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
