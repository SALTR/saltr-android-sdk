/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.exception;

public class SLTMissingFeaturesException extends SLTRuntimeException {
    public SLTMissingFeaturesException() {
        super("Features should be defined.");
    }
}
