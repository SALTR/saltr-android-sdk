/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import saltr.response.SLTResponseTemplate;

/**
 * This interface enables user to delegate his implementation of success and failure callbacks when registering device
 */
public interface SLTRegisterDeviceDelegate {
    public void onSuccess(SLTResponseTemplate response);

    public void onFailure();
}
