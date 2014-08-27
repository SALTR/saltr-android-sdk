/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr.exception;

public class SLTMissingSaltrUserIdException extends SLTRuntimeException {
    public SLTMissingSaltrUserIdException() {
        super("saltrUserId is missing");
    }
}
