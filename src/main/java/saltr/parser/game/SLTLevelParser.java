/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.parser.game;

import saltr.parser.response.level.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SLTLevelParser {

    public static Map<String, SLTMatchBoard> parseLevelContent(Map<String, SLTResponseBoard> boardNodes, Map<String, SLTAsset> assetMap) {
        Map<String, SLTMatchBoard> boards = new HashMap<>();
        for (Map.Entry<String, SLTResponseBoard> entry : boardNodes.entrySet()) {
            boards.put(entry.getKey(), parseLevelBoard(entry.getValue(), assetMap));
        }
        return boards;
    }

    private static SLTMatchBoard parseLevelBoard(SLTResponseBoard boardNode, Map<String, SLTAsset> assetMap) {
        Map<String, String> boardProperties = new HashMap<>();
        if (boardNode.getProperties() != null && boardNode.getProperties().getBoard() != null) {
            boardProperties = boardNode.getProperties().getBoard();
        }

        SLTCell[][] cells = new SLTCell[][]{};
        initializeCells(cells, boardNode);

        List<SLTBoardLayer> layers = new ArrayList<>();
        int i = 0;
        for (SLTResponseBoardLayer layerNode : boardNode.getLayers()) {
            SLTMatchBoardLayer layer = parseLayer(layerNode, i, cells, assetMap);
            layers.add(layer);
            i++;
        }

        return new SLTMatchBoard(cells, layers, boardProperties);
    }

    private static SLTMatchBoardLayer parseLayer(SLTResponseBoardLayer layerNode, int layerIndex, SLTCell[][] cells, Map<String, SLTAsset> assetMap) {
        String layerId = layerNode.getLayerId();
        SLTMatchBoardLayer layer = new SLTMatchBoardLayer(layerId, layerIndex);
        parseFixedAssets(layer, layerNode.getFixedAssets(), cells, assetMap);
        parseLayerChunks(layer, layerNode.getChunks(), cells, assetMap);
        return layer;
    }

    private static void initializeCells(SLTCell[][] cells, SLTResponseBoard boardNode) {
        List<List<Integer>> blockedCells;
        if (boardNode.getBlockedCells() != null && !boardNode.getBlockedCells().isEmpty()) {
            blockedCells = boardNode.getBlockedCells();
        }
        else {
            blockedCells = new ArrayList<>();
        }

        List<SLTResponseBoardPropertyCell> cellProperties;
        if (boardNode.getProperties() != null && boardNode.getProperties().getCell() != null) {
            cellProperties = boardNode.getProperties().getCell();
        }
        else {
            cellProperties = new ArrayList<>();
        }

        int rows = cells.length;
        int cols = cells[0].length;

        for (int i = 0; i < cols; ++i) {
            for (int j = 0; j < rows; ++j) {
                SLTCell cell = new SLTCell(i, j);
                cells[i][j] = cell;
            }
        }

        //assigning cell properties
        for (SLTResponseBoardPropertyCell property : cellProperties) {
            SLTCell cell2 = cells[property.getCoords().get(0)][property.getCoords().get(1)];
            if (cell2 != null) {
                cell2.setProperties(property.getValue());
            }
        }

        //blocking cells
        for (List<Integer> blockedCell : blockedCells) {
            SLTCell cell3 = cells[blockedCell.get(0)][blockedCell.get(1)];
            if (cell3 != null) {
                cell3.setIsBlocked(true);
            }
        }
    }

    private static void parseFixedAssets(SLTMatchBoardLayer layer, List<SLTResponseBoardFixedAsset> assetNodes, SLTCell[][] cells, Map<String, SLTAsset> assetMap) {
        for (SLTResponseBoardFixedAsset assetInstanceNode : assetNodes) {
            SLTAsset asset = assetMap.get(assetInstanceNode.getAssetId());
            List<String> stateIds = assetInstanceNode.getStates();
            int[][] cellPositions = assetInstanceNode.getCells();

            for (int[] position : cellPositions) {
                SLTCell cell = cells[position[0]][position[1]];
                cell.setAssetInstance(layer.getLayerId(), layer.getLayerIndex(), asset.getInstance(stateIds));
            }
        }
    }

    private static void parseLayerChunks(SLTMatchBoardLayer layer, List<SLTResponseBoardChunk> chunkNodes, SLTCell[][] cellMatrix, Map<String, SLTAsset> assetMap) {
        for (SLTResponseBoardChunk chunkNode : chunkNodes) {
            List<List<Integer>> cellNodes = chunkNode.getCells();
            List<SLTCell> chunkCells = new ArrayList<>();
            for (List<Integer> cellNode : cellNodes) {
                chunkCells.add(cellMatrix[cellNode.get(0)][cellNode.get(1)]);
            }

            List<SLTResponseBoardChunkAsset> assetNodes = chunkNode.getAssets();
            List<SLTChunkAssetRule> chunkAssetRules = new ArrayList<>();
            for (SLTResponseBoardChunkAsset assetNode : assetNodes) {
                chunkAssetRules.add(new SLTChunkAssetRule(assetNode.getAssetId(), assetNode.getDistributionType(),
                        assetNode.getDistributionValue(), assetNode.getStates()));
            }
            new SLTChunk(layer, chunkCells, chunkAssetRules, assetMap);
        }
    }

    public static Map<String, SLTAsset> parseLevelAssets(SLTResponseLevelData rootNode) {
        Map<String, SLTResponseAsset> assetNodes = rootNode.getAssets();
        Map<String, SLTAsset> assetMap = new HashMap<>();
        for (Map.Entry<String, SLTResponseAsset> entry : assetNodes.entrySet()) {
            assetMap.put(entry.getKey(), parseAsset(entry.getValue()));
        }
        return assetMap;
    }

    private static SLTAsset parseAsset(SLTResponseAsset assetNode) {
        String token = null;
        Object properties = null;
        Map statesMap = null;

        if (assetNode.getToken() != null) {
            token = assetNode.getToken();
        }

        if (assetNode.getStates() != null) {
            statesMap = parseAssetStates(assetNode.getStates());
        }

        if (assetNode.getProperties() != null) {
            properties = assetNode.getProperties();
        }

        return new SLTAsset(token, statesMap, properties);
    }

    private static Map<String, SLTAssetState> parseAssetStates(List<SLTResponseBoardChunkAssetState> stateNodes) {
        Map<String, SLTAssetState> statesMap = new HashMap<>();
        int index = 0;
        for (SLTResponseBoardChunkAssetState state : stateNodes) {
            statesMap.put(String.valueOf(index), parseAssetState(state));
            index++;
        }

        return statesMap;
    }

    private static SLTAssetState parseAssetState(SLTResponseBoardChunkAssetState stateNode) {
        String token = null;
        Object properties = null;

        if (stateNode.getToken() != null) {
            token = stateNode.getToken();
        }

        if (stateNode.getProperties() != null) {
            properties = stateNode.getProperties();
        }

        return new SLTAssetState(token, properties);
    }
}
