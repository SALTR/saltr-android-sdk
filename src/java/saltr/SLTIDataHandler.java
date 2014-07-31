/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.status.SLTStatus;

public interface SLTIDataHandler {
    public void onSuccess();

    public void onFailure(SLTStatus status);
}
