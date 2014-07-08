/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.game;

import java.util.List;
import java.util.Map;

public class SLTBoard {
    private Map<String, String> properties;
    protected List<SLTBoardLayer> layers;

    public SLTBoard(List<SLTBoardLayer> layers, Map<String, String> properties) {
        this.properties = properties;
        this.layers = layers;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public List<SLTBoardLayer> getLayers() {
        return layers;
    }
}
