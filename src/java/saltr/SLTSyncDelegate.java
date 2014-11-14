/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.response.SLTResponseClientData;

public interface SLTSyncDelegate {

    void onSuccess(SLTResponseClientData data);
}
