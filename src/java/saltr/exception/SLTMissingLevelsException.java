/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.exception;

public class SLTMissingLevelsException extends SLTRuntimeException {
    public SLTMissingLevelsException() {
        super("Levels should be imported.");
    }
}
