/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.status;

public class SLTStatusLevelsParseError extends SLTStatus {
    public SLTStatusLevelsParseError() {
        super(CLIENT_LEVELS_PARSE_ERROR, "[SALTR] Failed to decode Levels.");
    }
}
