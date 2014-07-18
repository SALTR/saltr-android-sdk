/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.game.canvas2d;

import saltr.game.SLTLevel;
import saltr.game.SLTLevelParser;

public class SLT2DLevel extends SLTLevel {
    public SLT2DLevel(String id, int index, int localIndex, int packIndex, String contentUrl, Object properties, String version) {
        super(id, index, localIndex, packIndex, contentUrl, properties, version);
    }

    public SLT2DBoard getBoard(String id) {
        return boards.get(id);
    }

    @Override
    protected SLTLevelParser getParser() {
        return SLT2DLevelParser.getInstance();
    }
}
