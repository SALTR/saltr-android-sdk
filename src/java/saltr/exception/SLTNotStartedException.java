/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr.exception;

/**
 * Created by daal on 8/27/14.
 */
public class SLTNotStartedException extends SLTRuntimeException {

    public SLTNotStartedException() {
        super("Saltr SDK is not started properly");
    }
}
