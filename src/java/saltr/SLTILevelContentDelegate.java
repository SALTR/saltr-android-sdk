/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr;

import saltr.game.SLTLevel;
import saltr.response.level.SLTResponseLevelContentData;

/**
 * Through this interface user delegates his own implementation of 'success' and 'failure' methods
 * to be called when on appropriate event occurs.
 */
public interface SLTILevelContentDelegate {
    /**
     * Method to be called when level successfully loaded.
     */
    void onSuccess(SLTResponseLevelContentData data, SLTLevel sltLevel);

    /**
     * Method to be called when level loading has failed.
     *
     * @param sltLevel Instance of {@link saltr.game.SLTLevel} class
     */
    void onFailure(SLTLevel sltLevel);
}
