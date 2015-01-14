/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.response.SLTResponseClientData;

/**
 * This interface enables user to delegate his implementation of success and failure callbacks when synchronizing data
 * with SALTR
 */
public interface SLTSyncDelegate {
    void onSuccess(SLTResponseClientData data);

    void onFailure();
}
