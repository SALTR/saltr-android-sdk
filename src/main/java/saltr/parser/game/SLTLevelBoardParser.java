/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

import saltr.parser.response.level.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SLTLevelBoardParser {

    public static Map<String, SLTLevelBoard> parseLevelBoards(Map<String, Board> boardNodes, SLTLevelSettings levelSettings) {
        Map<String, SLTLevelBoard> boards = new HashMap<>();
        for (Map.Entry<String, Board> entry : boardNodes.entrySet()) {
            boards.put(entry.getKey(), parseLevelBoard(entry.getValue(), levelSettings));
        }
        return boards;
    }

    public static SLTLevelBoard parseLevelBoard(Board boardNode, SLTLevelSettings levelSettings) {
        Map<String, String> boardProperties = null;
        SLTCell[][] cells = parseBoardCells(boardNode, levelSettings);
        if (boardNode.getProperties() != null && boardNode.getProperties().getBoard() != null) {
            boardProperties = boardNode.getProperties().getBoard();
        }
        return new SLTLevelBoard(cells, boardProperties);
    }

    private static SLTCell[][] parseBoardCells(Board boardNode, SLTLevelSettings levelSettings) {
        SLTCell[][] cells = new SLTCell[boardNode.getCols()][boardNode.getRows()];
        createEmptyBoard(cells, boardNode);
        Map<String, SLTCompositeInfo> composites = parseComposites(boardNode.getComposites(), cells, levelSettings);
        List<SLTChunk> boardChunks = parseChunks(boardNode.getChunks(), cells, levelSettings);
        generateComposites(composites);
        generateChunks(boardChunks);

        return cells;
    }

    private static void generateChunks(List<SLTChunk> chunks) {
        for (SLTChunk chunk : chunks) {
            chunk.generate();
        }
    }

    private static void generateComposites(Map<String, SLTCompositeInfo> composites) {
        for (Map.Entry<String, SLTCompositeInfo> entry : composites.entrySet()) {
            entry.getValue().generate();
        }
    }

    private static void createEmptyBoard(SLTCell[][] board, Board boardNode) {
        List<List<Integer>> blockedCells = boardNode.getBlockedCells() != null ? boardNode.getBlockedCells() : new ArrayList<List<Integer>>();
        List<CustomPropertyCell> cellProperties = boardNode.getProperties() != null && boardNode.getProperties().getCell() != null
                ? boardNode.getProperties().getCell() : new ArrayList<CustomPropertyCell>();
        int length;
        int cols = board.length;
        for (int j = 0; j < cols; ++j) {
            int rows = board[j].length;
            for (int i = 0; i < rows; ++i) {
                SLTCell cell = new SLTCell(j, i);
                board[j][i] = cell;
                length = cellProperties.size();
                for (int n = 0; n < length; n++) {
                    CustomPropertyCell property = cellProperties.get(n);
                    if (property.getCoords().get(0).equals(j) && property.getCoords().get(1).equals(1)) {
                        cell.setProperties(property.getValue());
                        break;
                    }
                }
                length = blockedCells.size();
                for (int n = 0; n < length; n++) {
                    List<Integer> blockedCell = blockedCells.get(n);
                    if (blockedCell.get(0).equals(j) && blockedCell.get(1).equals(i)) {
                        cell.setIsBocked(true);
                        break;
                    }
                }
            }
        }
    }

    private static List<SLTChunk> parseChunks(List<BoardChunk> chunkNodes, SLTCell[][] cellMatrix, SLTLevelSettings levelSettings) {
        List<SLTChunk> chunks = new ArrayList<>();
        List<List<Integer>> cellNodes;
        List<SLTCell> chunkCells;
        List<BoardChunkAsset> assetNodes;
        List<SLTChunkAssetInfo> chunkAssetInfoList;
        SLTChunk chunk;
        for (BoardChunk chunkNode : chunkNodes) {
            cellNodes = chunkNode.getCells();
            chunkCells = new ArrayList<>();
            for (List<Integer> cellNode : cellNodes) {
                chunkCells.add(cellMatrix[cellNode.get(1)][cellNode.get(0)]);
            }

            assetNodes = chunkNode.getAssets();
            chunkAssetInfoList = new ArrayList<>();
            for (BoardChunkAsset assetNode : assetNodes) {
                chunkAssetInfoList.add(new SLTChunkAssetInfo(assetNode.getAssetId(), assetNode.getCount(), assetNode.getStateId()));
            }

            chunk = new SLTChunk(chunkCells, chunkAssetInfoList, levelSettings);
            chunks.add(chunk);
        }
        return chunks;
    }


    private static Map<String, SLTCompositeInfo> parseComposites(List<BoardCompositeAsset> compositeNodes, SLTCell[][] cellMatrix, SLTLevelSettings levelSettings) {
        SLTCompositeInfo compositeInfo;
        Map<String, SLTCompositeInfo> compositesMap = new HashMap<>();
        for (BoardCompositeAsset compositeNode : compositeNodes) {
            List<Integer> cellPosition = compositeNode.getCell() != null ? compositeNode.getCell() : compositeNode.getPosition();
            compositeInfo = new SLTCompositeInfo(compositeNode.getAssetId(), compositeNode.getStateId(),
                    cellMatrix[cellPosition.get(1)][cellPosition.get(0)], levelSettings);
            compositesMap.put(compositeInfo.getAssetId(), compositeInfo);
        }
        return compositesMap;
    }

    public static SLTLevelSettings parseLevelSettings(LevelData rootNode) {
        return new SLTLevelSettings(parseBoardAssets(rootNode.getAssets()), rootNode.getKeySets(), rootNode.getAssetStates());
    }

    private static Map<String, SLTAsset> parseBoardAssets(Map<String, SaltrAsset> assetNodes) {
        Map<String, SLTAsset> assetMap = new HashMap<>();
        for (Map.Entry<String, SaltrAsset> entry : assetNodes.entrySet()) {
            assetMap.put(entry.getKey(), parseAsset(entry.getValue()));
        }
        return assetMap;
    }

    private static SLTAsset parseAsset(SaltrAsset assetNode) {
        String type = assetNode.getType() != null ? assetNode.getType() : assetNode.getType_key();
        if (assetNode.getCells() != null || assetNode.getCellInfos() != null) { /*if asset is composite asset*/
            List<List<Integer>> cellInfos = assetNode.getCellInfos() != null ? assetNode.getCellInfos() : assetNode.getCells();
            return new SLTCompositeAsset(cellInfos, type, assetNode.getKeys());
        }
        return new SLTAsset(type, assetNode.getKeys());
    }
}
