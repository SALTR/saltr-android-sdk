/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.response.level;

import java.util.List;
import java.util.Map;

public class SLTResponseAsset {
    private Map<String, Integer> keys;
    private String type_key;
    private String type;
    private List<Long> states;
    private List<List<Integer>> cells;
    private List<List<Integer>> cellInfos;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<List<Integer>> getCellInfos() {
        return cellInfos;
    }

    public void setCellInfos(List<List<Integer>> cellInfos) {
        this.cellInfos = cellInfos;
    }

    public Map<String, Integer> getKeys() {
        return keys;
    }

    public void setKeys(Map<String, Integer> keys) {
        this.keys = keys;
    }

    public String getType_key() {
        return type_key;
    }

    public void setType_key(String type_key) {
        this.type_key = type_key;
    }

    public List<Long> getStates() {
        return states;
    }

    public void setStates(List<Long> states) {
        this.states = states;
    }

    public List<List<Integer>> getCells() {
        return cells;
    }

    public void setCells(List<List<Integer>> cells) {
        this.cells = cells;
    }
}
