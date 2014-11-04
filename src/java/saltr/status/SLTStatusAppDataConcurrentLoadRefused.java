/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.status;

import saltr.SLTSaltr;

public class SLTStatusAppDataConcurrentLoadRefused extends SLTStatus {
    public SLTStatusAppDataConcurrentLoadRefused() {
        super(CLIENT_APP_DATA_CONCURRENT_LOAD_REFUSED, "[SALTR] appData load refused. Previous load is not complete");
    }
}
