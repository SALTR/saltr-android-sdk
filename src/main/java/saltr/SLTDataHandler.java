/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.status.SLTStatus;

public interface SLTDataHandler {
    public void onSuccess(SLTSaltr saltr);

    public void onFailure(SLTStatus status);
}
