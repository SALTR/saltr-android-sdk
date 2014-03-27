/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import saltr.parser.game.SLTLevel;
import saltr.parser.game.SLTLevelPack;
import saltr.parser.response.*;

import java.util.*;

public class SLTDeserializer {

    public static List<SLTExperiment> decodeExperiment(SLTResponseAppData data) {
        List<SLTExperiment> experiments = new ArrayList<>();
        List<SLTResponseExperiment> experimentInfoNodes = data.getExperimentInfo();
        if (experimentInfoNodes != null) {
            for (SLTResponseExperiment item : experimentInfoNodes) {
                String partition = item.getPartition() != null ? item.getPartition() : item.getPartitionName();
                SLTExperiment experiment = new SLTExperiment(partition, item.getToken(), item.getType(), item.getCustomEventList());
                experiments.add(experiment);
            }
        }
        return experiments;
    }

    public static List<SLTLevelPack> decodeLevels(SLTResponseAppData data) {

        List<SLTLevelPack> levelPacks = new ArrayList<>();
        List<SLTResponsePack> levelPackNodes = data.getLevelPacks() != null ? data.getLevelPacks() : data.getLevelPackList();
        List<SLTLevel> levels;
        List<SLTResponseLevel> levelNodes;
        for (SLTResponsePack levelPackNode : levelPackNodes) {
            levelNodes = levelPackNode.getLevelList();
            levels = new ArrayList<>();
            for (SLTResponseLevel levelNode : levelNodes) {
                int index = levelNode.getIndex() != null ? levelNode.getIndex() : levelNode.getOrder();
                levels.add(new SLTLevel(levelNode.getId().toString(), index, levelNode.getUrl(), levelNode.getProperties(), levelNode.getVersion().toString()));
            }
            Collections.sort(levels);
            int index = levelPackNode.getIndex() != null ? levelPackNode.getIndex() : levelPackNode.getOrder();
            levelPacks.add(new SLTLevelPack(levelPackNode.getToken(), index, levels));
        }
        Collections.sort(levelPacks);
        return levelPacks;
    }

    public static Map<String, SLTFeature> decodeFeatures(SLTResponseAppData data) {
        Map<String, SLTFeature> features = new HashMap<>();
        List<SLTResponseFeature> featuresNodes = data.getFeatures() != null ? data.getFeatures() : data.getFeatureList();
        if (featuresNodes != null) {
            SLTFeature feature;
            for (SLTResponseFeature featureObj : featuresNodes) {
                feature = new SLTFeature(featureObj.getToken(), featureObj.getData());
                features.put(featureObj.getToken(), feature);
            }
        }
        return features;
    }
}
