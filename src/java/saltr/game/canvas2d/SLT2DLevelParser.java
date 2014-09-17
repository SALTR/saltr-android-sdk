/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.*;
import saltr.response.level.SLTResponseBoard;
import saltr.response.level.SLTResponseBoardChunkAsset;
import saltr.response.level.SLTResponseBoardChunkAssetState;
import saltr.response.level.SLTResponseBoardLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SLT2DLevelParser extends SLTLevelParser {

    private static SLT2DLevelParser instance;

    private SLT2DLevelParser() {
    }

    public static SLT2DLevelParser getInstance() {
        if (instance == null) {
            instance = new SLT2DLevelParser();
        }
        return instance;
    }

    @Override
    public Map<String, SLTBoard> parseLevelContent(Map<String, SLTResponseBoard> boardNodes, Map<String, SLTAsset> assetMap) {
        Map<String, SLTBoard> boards = new HashMap<String, SLTBoard>();
        for (Map.Entry<String, SLTResponseBoard> entry : boardNodes.entrySet()) {
            boards.put(entry.getKey(), parseLevelBoard(entry.getValue(), assetMap));
        }
        return boards;
    }

    private SLT2DBoard parseLevelBoard(SLTResponseBoard boardNode, Map<String, SLTAsset> assetMap) {
        Map<String, Object> boardProperties = boardNode.getProperties();
        if (boardProperties == null) {
            boardProperties = new HashMap<String, Object>();
        }

        List<SLTBoardLayer> layers = new ArrayList<SLTBoardLayer>();
        int i = 0;
        for (SLTResponseBoardLayer layerNode : boardNode.getLayers()) {
            SLT2DBoardLayer layer = parseLayer(layerNode, i, assetMap);
            layers.add(layer);
            i++;
        }

        int width = boardNode.getWidth() != null ? boardNode.getWidth() : 0;
        int height = boardNode.getHeight() != null ? boardNode.getHeight() : 0;

        return new SLT2DBoard(width, height, layers, boardProperties);
    }

    private SLT2DBoardLayer parseLayer(SLTResponseBoardLayer layerNode, int layerIndex, Map<String, SLTAsset> assetMap) {
        SLT2DBoardLayer layer = new SLT2DBoardLayer(layerNode.getToken(), layerIndex);
        parseAssetInstances(layer, layerNode.getAssets(), assetMap);
        return layer;
    }

    private void parseAssetInstances(SLT2DBoardLayer layer, List<SLTResponseBoardChunkAsset> assetNodes, Map<String, SLTAsset> assetMap) {
        for (SLTResponseBoardChunkAsset assetInstanceNode : assetNodes) {
            SLTAsset asset = assetMap.get(assetInstanceNode.getAssetId());
            List<String> stateIds = assetInstanceNode.getStates();
            layer.addAssetInstance(new SLT2DAssetInstance(asset.getToken(), asset.getInstanceStates(stateIds),
                    asset.getProperties(), assetInstanceNode.getX(), assetInstanceNode.getY(), assetInstanceNode.getRotation()));
        }
    }

    @Override
    protected SLTAssetState parseAssetState(SLTResponseBoardChunkAssetState stateNode) {
        int pivotX = stateNode.getPivotX() != null ? stateNode.getPivotX() : 0;
        int pivotY = stateNode.getPivotY() != null ? stateNode.getPivotY() : 0;

        return new SLT2DAssetState(stateNode.getToken(), stateNode.getProperties(), pivotX, pivotY);
    }
}
