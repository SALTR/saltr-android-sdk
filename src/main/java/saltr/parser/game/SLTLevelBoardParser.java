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

    public static Map<String, SLTLevelBoard> parseLevelBoards(Map<String, SLTResponseBoard> boardNodes, SLTLevelSettings levelSettings) {
        Map<String, SLTLevelBoard> boards = new HashMap<>();
        for (Map.Entry<String, SLTResponseBoard> entry : boardNodes.entrySet()) {
            boards.put(entry.getKey(), parseLevelBoard(entry.getValue(), levelSettings));
        }
        return boards;
    }

    public static SLTLevelBoard parseLevelBoard(SLTResponseBoard boardNode, SLTLevelSettings levelSettings) {
        Map<String, String> boardProperties = new HashMap<>();
        if (boardNode.getProperties() != null && boardNode.getProperties().getBoard() != null) {
            boardProperties = boardNode.getProperties().getBoard();
        }

        SLTCell[][] cells = new SLTCell[][]{};
        initializeCells(cells, boardNode);

        Map<String, Integer> layers = new HashMap<>();
        int index = 0;
        for (SLTResponseBoardLayer layerNode : boardNode.getLayers()) {
            SLTLevelBoardLayer layer = new SLTLevelBoardLayer(layerNode.getLayerId(), index++, layerNode.getFixedAssets(),
                    layerNode.getChunks());
            parseLayer(layer, cells, levelSettings);
            layers.put(layer.getLayerId(), layer.getLayerIndex());
        }

        return new SLTLevelBoard(cells, layers, boardProperties);
    }

    private static void parseLayer(SLTLevelBoardLayer layer, SLTCell[][] cells, SLTLevelSettings levelSettings) {
        parseFixedAssets(layer, cells, levelSettings);
        parseChunks(layer, cells, levelSettings);
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
        for (int p = 0, pLen = cellProperties.size(); p < pLen; ++p) {
            SLTResponseBoardPropertyCell property = cellProperties.get(p);
            SLTCell cell2 = cells[property.getCoords().get(0)][property.getCoords().get(1)];
            if (cell2 != null) {
                cell2.setProperties(property.getValue());
            }
        }

        //blocking cells
        for (int b = 0, bLen = blockedCells.size(); b < bLen; ++b) {
            List<Integer> blockedCell = blockedCells.get(b);
            SLTCell cell3 = cells[blockedCell.get(0)][blockedCell.get(1)];
            ;
            if (cell3 != null) {
                cell3.setIsBlocked(true);
            }
        }
    }

    private static void parseFixedAssets(SLTLevelBoardLayer layer, SLTCell[][] cells, SLTLevelSettings levelSettings) {
        List<SLTResponseBoardFixedAsset> fixedAssetsNode = layer.getFixedAssetsNodes();
        Map<String, SLTAsset> assetMap = levelSettings.getAssetMap();
        Map<String, String> stateMap = levelSettings.getStateMap();

        for (int i = 0, iLen = fixedAssetsNode.size(); i < iLen; ++i) {
            SLTResponseBoardFixedAsset fixedAsset = fixedAssetsNode.get(i);
            SLTAsset asset = assetMap.get(fixedAsset.getAssetId());
            String state = stateMap.get(fixedAsset.getStateId());
            int[][] cellPositions = fixedAsset.getCells();

            for (int j = 0, jLen = cellPositions.length; j < jLen; ++j) {
                int[] position = cellPositions[j];
                SLTCell cell = cells[position[0]][position[1]];
                cell.setAssetInstance(layer.getLayerId(), layer.getLayerIndex(), new SLTAssetInstance(asset.getToken(), state, asset.getProperties()));
            }
        }
    }

    private static void parseChunks(SLTLevelBoardLayer layer, SLTCell[][] cellMatrix, SLTLevelSettings levelSettings) {
        List<SLTResponseBoardChunk> chunkNodes = layer.getChunkNodes();
        for (int i = 0, len = chunkNodes.size(); i < len; i++) {
            SLTResponseBoardChunk chunkNode = chunkNodes.get(i);
            List<List<Integer>> cellNodes = chunkNode.getCells();
            List<SLTCell> chunkCells = new ArrayList<>();
            for (List<Integer> cellNode : cellNodes) {
                chunkCells.add(cellMatrix[cellNode.get(0)][cellNode.get(1)]);
            }

            List<SLTResponseBoardChunkAsset> assetNodes = chunkNode.getAssets();
            List<SLTChunkAssetRule> chunkAssetRules = new ArrayList<>();
            for (SLTResponseBoardChunkAsset assetNode : assetNodes) {
                chunkAssetRules.add(new SLTChunkAssetRule(assetNode.getAssetId(), assetNode.getStateId(), assetNode.getDistributionType(), assetNode.getDistributionValue()));
            }
            new SLTChunk(layer, chunkCells, chunkAssetRules, levelSettings);
        }
    }

    public static SLTLevelSettings parseLevelSettings(SLTResponseLevelData rootNode) {
        return new SLTLevelSettings(parseBoardAssets(rootNode.getAssets()), rootNode.getAssetStates());
    }

    private static Map<String, SLTAsset> parseBoardAssets(Map<String, SLTResponseAsset> assetNodes) {
        Map<String, SLTAsset> assetMap = new HashMap<>();
        for (Map.Entry<String, SLTResponseAsset> entry : assetNodes.entrySet()) {
            assetMap.put(entry.getKey(), parseAsset(entry.getValue()));
        }
        return assetMap;
    }

    private static SLTAsset parseAsset(SLTResponseAsset assetNode) {
        String token = null;
        Object properties = null;

        if (assetNode.getToken() != null) {
            token = assetNode.getToken();
        }
        else if (assetNode.getType() != null) {
            token = assetNode.getType();
        }

        if (assetNode.getProperties() != null) {
            properties = assetNode.getProperties();
        }

        return new SLTAsset(token, properties);
    }
}
