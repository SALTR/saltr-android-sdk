/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.game.SLTLevel;
import saltr.response.level.SLTResponseLevelContentData;

public interface SLTILevelContentDelegate {

    void loadFromSaltrSuccessCallback(SLTResponseLevelContentData data, SLTLevel sltLevel);

    void loadFromSaltrFailCallback(SLTLevel sltLevel);
}
