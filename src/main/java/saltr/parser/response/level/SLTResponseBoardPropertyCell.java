/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.parser.response.level;

import java.util.List;
import java.util.Map;

public class SLTResponseBoardPropertyCell {
    private List<Integer> coords;
    private Map<String, String> value;

    public List<Integer> getCoords() {
        return coords;
    }

    public void setCoords(List<Integer> coords) {
        this.coords = coords;
    }

    public Map<String, String> getValue() {
        return value;
    }

    public void setValue(Map<String, String> value) {
        this.value = value;
    }
}
