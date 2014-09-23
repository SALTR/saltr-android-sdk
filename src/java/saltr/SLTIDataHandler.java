/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.status.SLTStatus;

/**
 * This interface enables user to handle callbacks from SDK.
 */
public interface SLTIDataHandler {
    public void onSuccess();

    public void onFailure(SLTStatus status);
}
