/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

import saltr.parser.game.SLTCompositeInfo;
import saltr.parser.gameeditor.simple.IAssetTemplate;
import saltr.parser.response.level.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SLTLevelBoardParser {

    public static void parseBoard(Object[][] outputBoard, Board board, SLTLevelSettings SLTLevelSettings) {
        createEmptyBoard(outputBoard);
        Map<String, SLTCompositeInfo> composites = parseComposites(board.getComposites(), outputBoard, SLTLevelSettings);
        Map boardChunks = parseChunks(board.getChunks(), outputBoard, SLTLevelSettings);
        generateComposites(composites);
        generateChunks(boardChunks);
    }

    public static void regenerateChunks(Object[][] outputBoard, Board board, SLTLevelSettings SLTLevelSettings) {
        Map<String, SLTChunk> boardChunks = parseChunks(board.getChunks(), outputBoard, SLTLevelSettings);
        generateChunks(boardChunks);
    }

    private static void generateChunks(Map<String, SLTChunk> chunks) {
        for (Map.Entry<String, SLTChunk> entry : chunks.entrySet()) {
            entry.getValue().generate();
        }
    }

    private static void generateComposites(Map<String, SLTCompositeInfo> composites) {
        for (Map.Entry<String, SLTCompositeInfo> entry : composites.entrySet()) {
            entry.getValue().generate();
        }
    }

    private static void createEmptyBoard(Object[][] board) {
        int cols = board.length;
        for (int j = 0; j < cols; ++j) {
            int rows = board[j].length;
            for (int i = 0; i < rows; ++i) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put("col", j);
                map.put("row", i);
                board[j][i] = map;
            }
        }
    }

    private static Map parseChunks(List<BoardChunk> chunksPrototype, Object[][] outputBoard, SLTLevelSettings SLTLevelSettings) {
        SLTChunkAssetInfo chunkAsset;
        SLTChunk chunk;
        List<BoardChunkAsset> assetsPrototype;
        List<List<Integer>> cellsPrototype;
        Map<String, SLTChunk> chunks = new HashMap<String, SLTChunk>();
        for (BoardChunk chunkPrototype : chunksPrototype) {
            chunk = new SLTChunk(chunkPrototype.getChunkId(), outputBoard, SLTLevelSettings);
            assetsPrototype = chunkPrototype.getAssets();
            for (BoardChunkAsset assetPrototype : assetsPrototype) {
                chunkAsset = new SLTChunkAssetInfo(assetPrototype.getAssetId(), assetPrototype.getCount(), assetPrototype.getStateId());
                chunk.addChunkAsset(chunkAsset);
            }
            cellsPrototype = chunkPrototype.getCells();
            for (List<Integer> cellPrototype : cellsPrototype) {
                chunk.addCell(new SLTCell(cellPrototype.get(0), cellPrototype.get(1)));
            }
            chunks.put(chunk.getId(), chunk);
        }
        return chunks;
    }


    private static Map<String, SLTCompositeInfo> parseComposites(List<BoardCompositeAsset> compositeNodes, Object[][] cellMatrix, SLTLevelSettings levelSettings) {
        SLTCompositeInfo compositeInfo;
        Map<String, SLTCompositeInfo> compositesMap = new HashMap<>();
        for (BoardCompositeAsset compositePrototype : compositeNodes) {
            List<Integer> cellPosition = compositePrototype.getCell() != null ? compositePrototype.getCell() : compositePrototype.getPosition();
            compositeInfo = new SLTCompositeInfo(compositePrototype.getAssetId(), new SLTCell(cellPosition.get(0), cellPosition.get(1)), cellMatrix, levelSettings);
            compositesMap.put(compositeInfo.getAssetId(), compositeInfo);
        }
        return compositesMap;
    }

    public static SLTLevelSettings parseLevelSettings(LevelData rootNode) {
        return new SLTLevelSettings(parseBoardAssets(rootNode.getAssets()), rootNode.getKeySets(), rootNode.getAssetStates());
    }

    private static Map<String, IAssetTemplate> parseBoardAssets(Map<String, SaltrAsset> assetNodes) {
        Map<String, IAssetTemplate> assetMap = new HashMap<>();
        for (Map.Entry<String, SaltrAsset> entry : assetNodes.entrySet()) {
            assetMap.put(entry.getKey(), parseAsset(entry.getValue()));
        }
        return assetMap;
    }

    private static IAssetTemplate parseAsset(SaltrAsset assetNode) {
        String type = assetNode.getType() != null ? assetNode.getType() : assetNode.getType_key();
        if (assetNode.getCells() != null || assetNode.getCellInfos() != null) { /*if asset is composite asset*/
            List<List<Integer>> cellInfos = assetNode.getCellInfos() != null ? assetNode.getCellInfos() : assetNode.getCells();
            return new SLTCompositeAsset(cellInfos, type, assetNode.getKeys());
        }
        return new SLTAsset(type, assetNode.getKeys());
    }
}
