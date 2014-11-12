/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.response.SLTResponseAppData;

interface SLTIAppDataDelegate {

    void onSuccess(SLTResponseAppData response);

    void onFailure();
}
