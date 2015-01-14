/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import java.util.List;


/**
 * The SLTExperiment class provides the currently running experiment data.
 * It is possible to A/B test any feature included in the game AND/OR different levels, level packs.
 */
public class SLTExperiment {
    /**
     * Specifies the Feature type for the experiment.
     */
    public static final String SPLIT_TEST_TYPE_FEATURE = "FEATURE";
    /**
     * Specifies the LevelPack type for the experiment.
     */
    public static final String SPLIT_TEST_TYPE_LEVEL_PACK = "LEVEL_PACK";

    private String partition;
    private String token;
    private String type;
    private List<String> customEvents;

    /**
     * Class constructor.
     *
     * @param token        The unique identifier of the experiment.
     * @param partition    The letter of the partition in which the user included in (A, B, C, etc.).
     * @param type         The type of the experiment (Feature or LevelPack).
     * @param customEvents The array of comma separated event names for which A/B test data should be send.
     */
    public SLTExperiment(String partition, String token, String type, List<String> customEvents) {
        this.partition = partition;
        this.token = token;
        this.type = type;
        this.customEvents = customEvents;
    }

    /**
     * @return The letter of the partition in which the user included in (A, B, C, etc.).
     */
    public String getPartition() {
        return partition;
    }

    /**
     * @return The unique identifier of the experiment.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return The type of the experiment (Feature or LevelPack).
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return The array of comma separated event names for which A/B test data should be send.
     */
    public List<String> getCustomEvents() {
        return customEvents;
    }
}
