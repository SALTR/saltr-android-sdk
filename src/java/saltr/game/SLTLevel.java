/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game;

import saltr.response.level.SLTResponseBoard;
import saltr.response.level.SLTResponseLevelData;

import java.util.Map;

public class SLTLevel implements Comparable<SLTLevel> {
    private String id;
    private String contentUrl;
    private int index;
    private Object properties;
    private Map<String, SLTMatchBoard> boards;
    private Boolean contentReady;
    private String version;
    private int packIndex;
    private int localIndex;
    private Map<String, SLTAsset> assetMap;

    public SLTLevel(String id, int index, int localIndex, int packIndex, String contentUrl, Object properties, String version) {
        this.id = id;
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

    public SLTMatchBoard getBoard(String id) {
        return boards.get(id);
    }

    public void updateContent(SLTResponseLevelData rootNode) throws Exception {
        Map<String, SLTResponseBoard> boardsNode = null;
        if (rootNode.getBoards() != null) {
            boardsNode = rootNode.getBoards();
        }
        else {
            throw new Exception("[SALTR: ERROR] Level content's 'boards' node can not be found.");
        }

        properties = rootNode.getProperties();

        try {
            assetMap = SLTLevelParser.parseLevelAssets(rootNode);
        } catch (Exception e) {
            throw new Exception("[SALTR: ERROR] Level content boards parsing failed.");
        }

        try {
            boards = SLTLevelParser.parseLevelContent(boardsNode, assetMap);
        } catch (Exception e) {
            throw new Exception("[SALTR: ERROR] Level content boards parsing failed.");
        }

        regenerateAllBoards();
        contentReady = true;
    }

    public void regenerateAllBoards() {
        for (Map.Entry<String, SLTMatchBoard> entry : boards.entrySet()) {
            entry.getValue().regenerateChunks();
        }
    }

    public void regenerateBoard(String boardId) {
        if (boards != null && boards.get(boardId) != null) {
            SLTMatchBoard board = boards.get(boardId);
            board.regenerateChunks();
        }
    }

    @Override
    public int compareTo(SLTLevel o) {
        return (new Integer(this.index)).compareTo(o.index);
    }
}
