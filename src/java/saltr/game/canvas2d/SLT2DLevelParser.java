/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTAsset;
import saltr.game.SLTBoard;
import saltr.game.SLTLevelParser;
import saltr.response.level.SLTResponseBoard;

import java.util.HashMap;
import java.util.Map;

public class SLT2DLevelParser extends SLTLevelParser {

    private static SLT2DLevelParser instance;

    private SLT2DLevelParser() {}

    public static SLT2DLevelParser getInstance() {
        if (instance == null) {
            instance = new SLT2DLevelParser();
        }
        return instance;
    }

    @Override
    public Map<String, SLT2DBoard> parseLevelContent(Map<String, SLTResponseBoard> boardNodes, Map<String, SLTAsset> assetMap) {
        Map<String, SLTBoard> boards = new HashMap<>();
        for (Map.Entry<String, SLTResponseBoard> entry : boardNodes.entrySet()) {
            boards.put(entry.getKey(), parseLevelBoard(entry.getValue(), assetMap));
        }
        return boards;
    }

    /*private SLT2DBoard parseLevelBoard(SLTResponseBoard boardNode, assetMap:Dictionary) {
        var boardProperties:Object = {};
        if (boardNode.hasOwnProperty("properties") && boardNode.properties.hasOwnProperty("board")) {
            boardProperties = boardNode.properties.board;
        }

        var layers:Vector.<SLTBoardLayer> = new Vector.<SLTBoardLayer>();
        var layerNodes:Array = boardNode.layers;
        for (var i:int = 0, len:int = layerNodes.length; i < len; ++i) {
            var layerNode:Object = layerNodes[i];
            var layer:SLT2DBoardLayer = parseLayer(layerNode, i, assetMap);
            layers.push(layer);
        }

        var width:Number = boardNode.hasOwnProperty("width") ? boardNode.width : 0;
        var height:Number = boardNode.hasOwnProperty("height") ? boardNode.height : 0;

        return new SLT2DBoard(width, height, layers, boardProperties);
    }*/
}
