/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser;

import saltr.parser.data.Vector2D;
import saltr.parser.gameeditor.BoardData;
import saltr.parser.gameeditor.Cell;
import saltr.parser.gameeditor.chunk.AssetInChunk;
import saltr.parser.gameeditor.chunk.Chunk;
import saltr.parser.gameeditor.composite.Composite;
import saltr.parser.gameeditor.composite.CompositeAssetTemplate;
import saltr.parser.gameeditor.simple.SimpleAssetTemplate;
import saltr.parser.response.level.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelParser {
    public static void parseBoard(Vector2D outputBoard, Board board, BoardData boardData) {
        createEmptyBoard(outputBoard);
        Map<String, Composite> composites = parseComposites(board.getComposites(), outputBoard, boardData);
        Map boardChunks = parseChunks(board.getChunks(), outputBoard, boardData);
        generateComposites(composites);
        generateChunks(boardChunks);
    }

    public static void regenerateChunks(Vector2D outputBoard, Board board, BoardData boardData) {
        Map<String, Chunk> boardChunks = parseChunks(board.getChunks(), outputBoard, boardData);
        generateChunks(boardChunks);
    }

    private static void generateChunks(Map<String, Chunk> chunks) {
        for (Map.Entry<String, Chunk> entry : chunks.entrySet()) {
            entry.getValue().generate();
        }
    }

    private static void generateComposites(Map<String, Composite> composites) {
        for (Map.Entry<String, Composite> entry : composites.entrySet()) {
            entry.getValue().generate();
        }
    }

    private static void createEmptyBoard(Vector2D board) {
        int cols = board.getWidth();
        int rows = board.getHeight();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put("col", j);
                map.put("row", i);
                board.insert(j, i, map);
            }
        }
    }

    private static Map parseChunks(List<BoardChunk> chunksPrototype, Vector2D outputBoard, BoardData boardData) {
        AssetInChunk chunkAsset;
        Chunk chunk;
        List<BoardChunkAsset> assetsPrototype;
        List<List<Integer>> cellsPrototype;
        Map<String, Chunk> chunks = new HashMap<String, Chunk>();
        for (BoardChunk chunkPrototype : chunksPrototype) {
            chunk = new Chunk(chunkPrototype.getChunkId(), outputBoard, boardData);
            assetsPrototype = chunkPrototype.getAssets();
            for (BoardChunkAsset assetPrototype : assetsPrototype) {
                chunkAsset = new AssetInChunk(assetPrototype.getAssetId(), assetPrototype.getCount(), assetPrototype.getStateId());
                chunk.addChunkAsset(chunkAsset);
            }
            cellsPrototype = chunkPrototype.getCells();
            for (List<Integer> cellPrototype : cellsPrototype) {
                chunk.addCell(new Cell(cellPrototype.get(0), cellPrototype.get(1)));
            }
            chunks.put(chunk.getId(), chunk);
        }
        return chunks;
    }


    private static Map<String, Composite> parseComposites(List<BoardCompositeAsset> composites, Vector2D outputBoard, BoardData boardData) {
        Composite composite;
        Map<String, Composite> compositesMap = new HashMap<String, Composite>();
        for (BoardCompositeAsset compositePrototype : composites) {
            composite = new Composite(compositePrototype.getAssetId(), new Cell(compositePrototype.getPosition().get(0), compositePrototype.getPosition().get(1)), outputBoard, boardData);
            compositesMap.put(composite.getId(), composite);
        }
        return compositesMap;
    }

    public static BoardData parseBoardData(LevelData data) {
        BoardData boardData = new BoardData();
        boardData.setAssetMap(parseBoardAssets(data.getAssets()));
        boardData.setKeyset(data.getKeySets());
        boardData.setStateMap(data.getAssetStates());
        return boardData;
    }

    private static Map parseBoardAssets(Map<String, SaltrAsset> assets) {
        Map assetMap = new HashMap();
        for (Map.Entry<String, SaltrAsset> entry : assets.entrySet()) {
            assetMap.put(entry.getKey(), parseAsset(entry.getValue()));
        }
        return assetMap;

    }

    private static SimpleAssetTemplate parseAsset(SaltrAsset asset) {
        if (asset.getCells() != null) { /*if asset is composite asset*/
            return new CompositeAssetTemplate(asset.getCells(), asset.getType_key(), asset.getKeys());
        }
        return new SimpleAssetTemplate(asset.getType_key(), asset.getKeys());
    }
}
