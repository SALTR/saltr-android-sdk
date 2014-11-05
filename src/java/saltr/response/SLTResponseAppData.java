/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response;

import java.io.Serializable;
import java.util.List;

public class SLTResponseAppData implements Serializable {
    private List<SLTResponsePack> levelPacks;
    private List<SLTResponseFeature> features;
    private List<SLTResponseExperiment> experiments;
    private String levelType;
    private boolean success;
    private SLTResponseError error;

    public SLTResponseError getError() {
        return error;
    }

    public void setError(SLTResponseError error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

    public List<SLTResponseExperiment> getExperiments() {
        return experiments;
    }

    public void setExperiments(List<SLTResponseExperiment> experiments) {
        this.experiments = experiments;
    }

    public List<SLTResponseFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<SLTResponseFeature> features) {
        this.features = features;
    }

    public List<SLTResponsePack> getLevelPacks() {
        return levelPacks;
    }

    public void setLevelPacks(List<SLTResponsePack> levelPacks) {
        this.levelPacks = levelPacks;
    }
}
