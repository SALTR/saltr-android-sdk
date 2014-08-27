/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr.exception;

/**
 * Created by daal on 8/27/14.
 */
public class SLTLevelParserNullPointerException extends SLTException {
    public SLTLevelParserNullPointerException() {
        super("[SALTR] Failed to find parser for current level type..");
    }
}
