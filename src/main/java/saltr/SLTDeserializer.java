/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import saltr.parser.game.SLTLevel;
import saltr.parser.game.SLTLevelPack;
import saltr.parser.response.*;

import java.util.*;

public class SLTDeserializer {

    public static List<SLTExperiment> decodeExperiments(SLTResponseAppData rootNode) {
        List<SLTExperiment> experiments = new ArrayList<>();
        List<SLTResponseExperiment> experimentNodes = rootNode.getExperiments() != null ? rootNode.getExperiments() : rootNode.getExperimentInfo();

        if (experimentNodes != null) {
            for (SLTResponseExperiment experimentNode : experimentNodes) {
                String token = experimentNode.getToken();
                String partition = experimentNode.getPartition() != null ? experimentNode.getPartition() : experimentNode.getPartitionName();
                String type = experimentNode.getType();
                List<String> customEvents = experimentNode.getCustomEventList();
                experiments.add(new SLTExperiment(token, partition, type, customEvents));
            }
        }
        return experiments;
    }

    public static List<SLTLevelPack> decodeLevels(SLTResponseAppData data) {

        List<SLTResponsePack> levelPackNodes = data.getLevelPacks();
        List<SLTLevelPack> levelPacks = new ArrayList<>();


        int index = -1;
        if (levelPackNodes != null) {
            for (SLTResponsePack levelPackNode : levelPackNodes) {
                List<SLTResponseLevel> levelNodes = levelPackNode.getLevels();
                List<SLTLevel> levels = new ArrayList<>();
                int packIndex = levelPackNode.getIndex();
                for (SLTResponseLevel levelNode : levelNodes) {
                    ++index;
                    int localIndex = levelNode.getIndex() != null ? levelNode.getIndex() : levelNode.getLocalIndex();
                    levels.add(new SLTLevel(levelNode.getId(), index, localIndex, packIndex, levelNode.getUrl(),
                            levelNode.getProperties(), levelNode.getVersion().toString()));
                }
                Collections.sort(levels);
                levelPacks.add(new SLTLevelPack(levelPackNode.getToken(), packIndex, levels));
            }
        }
        Collections.sort(levelPacks);
        return levelPacks;
    }

    public static Map<String, SLTFeature> decodeFeatures(SLTResponseAppData data) {
        Map<String, SLTFeature> features = new HashMap<>();
        List<SLTResponseFeature> featuresNodes = data.getFeatures();
        if (featuresNodes != null) {
            SLTFeature feature;
            for (SLTResponseFeature featureNode : featuresNodes) {
                feature = new SLTFeature(featureNode.getToken(), featureNode.getData(), featureNode.getRequired());
                features.put(featureNode.getToken(), feature);
            }
        }
        return features;
    }
}
