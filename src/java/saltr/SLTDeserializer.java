/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

import saltr.game.SLTLevel;
import saltr.game.SLTLevelPack;
import saltr.response.*;

import java.util.*;

public class SLTDeserializer {

    public static List<SLTExperiment> decodeExperiments(SLTResponseAppData rootNode) {
        List<SLTExperiment> experiments = new ArrayList<SLTExperiment>();
        List<SLTResponseExperiment> experimentNodes = rootNode.getExperiments();

        if (experimentNodes != null) {
            for (SLTResponseExperiment experimentNode : experimentNodes) {

                List<String> customEvents = experimentNode.getCustomEventList();
                experiments.add(new SLTExperiment(experimentNode.getToken(), experimentNode.getPartition(), experimentNode.getType(), customEvents));
            }
        }
        return experiments;
    }

    public static List<SLTLevelPack> decodeLevels(SLTResponseAppData rootNode) {

        List<SLTResponsePack> levelPackNodes = rootNode.getLevelPacks();
        String levelType = SLTLevel.LEVEL_TYPE_MATCHING;

        if (rootNode.getLevelType() != null) {
            levelType = rootNode.getLevelType();
        }

        List<SLTLevelPack> levelPacks = new ArrayList<SLTLevelPack>();
        int index = -1;
        if (levelPackNodes != null) {
            for (SLTResponsePack levelPackNode : levelPackNodes) {
                List<SLTResponseLevel> levelNodes = levelPackNode.getLevels();
                List<SLTLevel> levels = new ArrayList<SLTLevel>();
                int packIndex = levelPackNode.getIndex();
                for (SLTResponseLevel levelNode : levelNodes) {
                    ++index;
                    levels.add(new SLTLevel(levelNode.getId(), levelType, index, levelNode.getIndex(), packIndex, levelNode.getUrl(),
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
        Map<String, SLTFeature> features = new HashMap<String, SLTFeature>();
        List<SLTResponseFeature> featuresNodes = data.getFeatures();
        if (featuresNodes != null) {
            for (SLTResponseFeature featureNode : featuresNodes) {
                Map<String, Object> properties = featureNode.getProperties();
                SLTFeature feature = new SLTFeature(featureNode.getToken(), properties, featureNode.getRequired());
                features.put(featureNode.getToken(), feature);
            }
        }
        return features;
    }
}
