/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.response;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class SLTResponseAppData implements Serializable {
    private List<SLTResponsePack> levelPackList;
    private List<SLTResponsePack> levelPacks;
    private List<SLTResponseFeature> featureList;
    private List<SLTResponseFeature> features;
    private List<SLTResponseExperiment> experimentInfo;
    private List<SLTResponseExperiment> experiments;
    private UUID saltId;
    private UUID saltrUserId;

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

    public List<SLTResponsePack> getLevelPackList() {
        return levelPackList;
    }

    public void setLevelPackList(List<SLTResponsePack> levelPackList) {
        this.levelPackList = levelPackList;
    }

    public List<SLTResponseFeature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<SLTResponseFeature> featureList) {
        this.featureList = featureList;
    }

    public List<SLTResponseExperiment> getExperimentInfo() {
        return experimentInfo;
    }

    public void setExperimentInfo(List<SLTResponseExperiment> experimentInfo) {
        this.experimentInfo = experimentInfo;
    }

    public UUID getSaltId() {
        return saltId;
    }

    public void setSaltId(UUID saltId) {
        this.saltId = saltId;
    }

    public UUID getSaltrUserId() {
        return saltrUserId;
    }

    public void setSaltrUserId(UUID saltrUserId) {
        this.saltrUserId = saltrUserId;
    }
}
