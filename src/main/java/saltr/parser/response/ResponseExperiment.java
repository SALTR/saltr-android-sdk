/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.parser.response;

import java.util.List;

public class ResponseExperiment {
    private String name;
    private String token;
    private String partitionName;
    private ExperimentType type;
    private ExperimentTrackingType trackingType;
    private List<String> customEventList;

    public ExperimentTrackingType getTrackingType() {
        return trackingType;
    }

    public void setTrackingType(ExperimentTrackingType trackingType) {
        this.trackingType = trackingType;
    }

    public List<String> getCustomEventList() {
        return customEventList;
    }

    public void setCustomEventList(List<String> customEventList) {
        this.customEventList = customEventList;
    }

    public ExperimentType getType() {
        return type;
    }

    public void setType(ExperimentType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartitionName() {
        return partitionName;
    }

    public void setPartitionName(String partitionName) {
        this.partitionName = partitionName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
