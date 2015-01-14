/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.status;

/**
 * The SLTStatusLevelsParserMissing class represents the client levels parse error.
 */
public class SLTStatusLevelsParserMissing extends SLTStatus {
    public SLTStatusLevelsParserMissing() {
        super(CLIENT_LEVELS_PARSE_ERROR, "[SALTR] Failed to find parser for current level type..");
    }
}
