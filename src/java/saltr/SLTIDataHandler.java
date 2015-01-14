/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.status.SLTStatus;

/**
 * Through this interface user delegates his own implementation of 'success' and 'failure' methods
 * to be called when on appropriate event occurs.
 */
public interface SLTIDataHandler {
    /**
     * Method to be called on success.
     */
    public void onSuccess();

    /**
     * Method to be called on failure
     *
     * @param status Instance of {@link saltr.status.SLTStatus} class
     */
    public void onFailure(SLTStatus status);
}
