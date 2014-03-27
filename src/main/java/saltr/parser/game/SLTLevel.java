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
    private String contentDataUrl;
    private int index;
    private Object properties;
    private Map<String, SLTLevelBoard> boards;
    private Boolean contentReady;
    private String version;
    private SLTResponseLevelData rootNode;
    private SLTLevelSettings levelSettings;
    private Map<String, SLTResponseBoard> boardsNode;

    public SLTLevel(String id, int index, String contentDataUrl, Object properties, String version) {
        this.id = id;
        this.index = index;
        this.contentDataUrl = contentDataUrl;
        this.contentReady = false;
        this.properties = properties;
        this.version = version;
    }

    public SLTLevelSettings getLevelSettings() {
        return levelSettings;
    }

    public String getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public Object getProperties() {
        return properties;
    }

    public Boolean getContentReady() {
        return contentReady;
    }

    public String getContentDataUrl() {
        return contentDataUrl;
    }

    public String getVersion() {
        return version;
    }

    public SLTLevelBoard getBoard(String id) {
        return boards.get(id);
    }

    public void updateContent(SLTResponseLevelData rootNode) {
        this.rootNode = rootNode;
        boardsNode = rootNode.getBoards();
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
