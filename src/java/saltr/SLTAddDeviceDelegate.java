/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import saltr.response.SLTResponseTemplate;
import saltr.status.SLTStatus;

public interface SLTAddDeviceDelegate {
    public void onSuccess(SLTResponseTemplate response);

    public void onFailure();
}
