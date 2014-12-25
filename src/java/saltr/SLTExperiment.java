/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import java.util.List;

/**
 * Represents an experiment.
 */
public class SLTExperiment {
    public static final String SPLIT_TEST_TYPE_FEATURE = "FEATURE";
    public static final String SPLIT_TEST_TYPE_LEVEL_PACK = "LEVEL_PACK";

    private String partition;
    private String token;
    private String type;
    private List<String> customEvents;

    public SLTExperiment(String partition, String token, String type, List<String> customEvents) {
        this.partition = partition;
        this.token = token;
        this.type = type;
        this.customEvents = customEvents;
    }

    /**
     * @return the partition letter the client is assign to (<code>"A"</code>, <code>"B"</code>, <code>"C"</code>, etc.).
     */
    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    /**
     * @return the token -  a unique identifier for the experiment.
     */
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the type of the experiment.
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getCustomEvents() {
        return customEvents;
    }

    public void setCustomEvents(List<String> customEvents) {
        this.customEvents = customEvents;
    }
}
