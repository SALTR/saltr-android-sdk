/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.response.SLTResponseAppData;

interface SLTIAppDataDelegate {

    void appDataLoadSuccessCallback(SLTResponseAppData response);

    void appDataLoadFailCallback();
}
