/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.response.SLTResponse;
import saltr.response.SLTResponseAppData;

interface SLTIAppDataDelegate {

    void appDataLoadSuccessCallback(SLTResponse<SLTResponseAppData> response);
    void appDataLoadFailCallback();
}
