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

public class AppData implements Serializable {
    private List<ResponsePack> levelPackList;
    private List<ResponsePack> levelPacks;
    private List<ResponseFeature> featureList;
    private List<ResponseFeature> features;
    private List<ResponseExperiment> experimentInfo;
    private UUID saltId;
    private UUID saltrUserId;

    public List<ResponseFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<ResponseFeature> features) {
        this.features = features;
    }

    public List<ResponsePack> getLevelPacks() {
        return levelPacks;
    }

    public void setLevelPacks(List<ResponsePack> levelPacks) {
        this.levelPacks = levelPacks;
    }

    public List<ResponsePack> getLevelPackList() {
        return levelPackList;
    }

    public void setLevelPackList(List<ResponsePack> levelPackList) {
        this.levelPackList = levelPackList;
    }

    public List<ResponseFeature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<ResponseFeature> featureList) {
        this.featureList = featureList;
    }

    public List<ResponseExperiment> getExperimentInfo() {
        return experimentInfo;
    }

    public void setExperimentInfo(List<ResponseExperiment> experimentInfo) {
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
