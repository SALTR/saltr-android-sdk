/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

public class SLTBoardLayer {
    private String layerId;
    private int layerIndex;

    public SLTBoardLayer(String layerId, int layerIndex) {
        this.layerId = layerId;
        this.layerIndex = layerIndex;
    }

    public String getLayerId() {
        return layerId;
    }

    public int getLayerIndex() {
        return layerIndex;
    }
}
