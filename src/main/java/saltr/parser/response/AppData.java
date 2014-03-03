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

public class AppData implements Serializable{
    private List<ResponsePack> levelPackList;
    private List<ResponseFeature> featureList;
    private List<ResponseExperiment> splitTestInfo;
    private UUID saltId;

    public UUID getSaltId() {
        return saltId;
    }

    public void setSaltId(UUID saltId) {
        this.saltId = saltId;
    }

    public List<ResponseFeature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<ResponseFeature> featureList) {
        this.featureList = featureList;
    }

    public List<ResponseExperiment> getExperiment() {
        return splitTestInfo;
    }

    public void setSplitTestInfo(List<ResponseExperiment> splitTestInfo) {
        this.splitTestInfo = splitTestInfo;
    }

    public List<ResponsePack> getLevelPackList() {
        return levelPackList;
    }

    public void setLevelPackList(List<ResponsePack> levelPackList) {
        this.levelPackList = levelPackList;
    }
}
