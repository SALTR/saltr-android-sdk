/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.exception;

public class SLTMissingDeviceIdException extends SLTRuntimeException {
    public SLTMissingDeviceIdException() {
        super("deviceId field is required and can't be null.");
    }
}
