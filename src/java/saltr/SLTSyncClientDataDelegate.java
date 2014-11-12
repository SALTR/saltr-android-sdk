/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.response.SLTResponseClientData;

public interface SLTSyncClientDataDelegate {

    void onSuccess(SLTResponseClientData data);
}
