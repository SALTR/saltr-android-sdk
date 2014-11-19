/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.game.SLTLevelPack;
import saltr.response.SLTResponseAppData;
import saltr.status.SLTStatus;

import java.util.List;
import java.util.Map;

interface SLTIAppDataDelegate {

    void onSuccess(SLTResponseAppData response, Map<String, SLTFeature> features, List<SLTExperiment> experiments, List<SLTLevelPack> levels);

    void onFailure(SLTStatus status);
}
