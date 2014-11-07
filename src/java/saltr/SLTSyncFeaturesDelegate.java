/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.game.SLTLevel;
import saltr.response.SLTResponse;
import saltr.response.level.SLTResponseLevelContentData;

public interface SLTSyncFeaturesDelegate {

    void syncFeaturesSuccessCallback(SLTResponse data);
}
