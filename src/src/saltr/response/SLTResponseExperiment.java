/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.response;

import java.io.Serializable;
import java.util.List;

public class SLTResponseExperiment implements Serializable {
    private String name;
    private String token;
    private String partitionName;
    private String partition;
    private String type;
    private String trackingType;
    private List<String> customEventList;

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public List<String> getCustomEventList() {
        return customEventList;
    }

    public void setCustomEventList(List<String> customEventList) {
        this.customEventList = customEventList;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrackingType() {
        return trackingType;
    }

    public void setTrackingType(String trackingType) {
        this.trackingType = trackingType;
    }
}
