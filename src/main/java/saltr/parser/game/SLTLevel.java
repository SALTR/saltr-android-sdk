/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

import saltr.parser.response.level.SLTResponseBoard;
import saltr.parser.response.level.SLTResponseLevelData;

import java.util.Map;

public class SLTLevel implements Comparable<SLTLevel> {
    private String id;
    private String contentUrl;
    private int index;
    private Object properties;
    private Map<String, SLTLevelBoard> boards;
    private Boolean contentReady;
    private String version;
    private SLTResponseLevelData rootNode;
    private SLTLevelSettings levelSettings;
    private Map<String, SLTResponseBoard> boardsNode;
    private int packIndex;
    private int localIndex;

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

    public SLTLevelBoard getBoard(String id) {
        return boards.get(id);
    }

    public void updateContent(SLTResponseLevelData rootNode) throws Exception {
        this.rootNode = rootNode;

        if (rootNode.getBoards() != null) {
            boardsNode = rootNode.getBoards();
        }
        else {
            throw new Exception("Boards node is not found.");
        }

        properties = rootNode.getProperties();
        levelSettings = SLTLevelBoardParser.parseLevelSettings(rootNode);
        generateAllBoards();
        contentReady = true;
    }

    public void generateAllBoards() {
        if (boardsNode != null) {
            boards = SLTLevelBoardParser.parseLevelBoards(boardsNode, levelSettings);
        }
    }

    public void generateBoard(String boardId) {
        if (boardsNode != null && boardsNode.get(boardId) != null) {
            boards.put(boardId, SLTLevelBoardParser.parseLevelBoard(boardsNode.get(boardId), levelSettings));
        }
    }

    @Override
    public int compareTo(SLTLevel o) {
        return (new Integer(this.index)).compareTo(o.index);
    }
}
