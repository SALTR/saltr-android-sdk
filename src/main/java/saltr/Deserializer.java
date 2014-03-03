/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import saltr.parser.response.*;

import java.util.*;

public class Deserializer {

    public List<Experiment> decodeExperimentInfo(AppData data) {
        List<Experiment> experiments = new ArrayList<Experiment>();

        if (data.getExperiment() != null) {
            for (ResponseExperiment item : data.getExperiment()) {
                Experiment experiment = new Experiment();
                experiment.setToken(item.getToken());
                experiment.setPartition(item.getPartitionName());
                experiment.setType(item.getType());
                experiment.setCustomEvents(item.getCustomEventList());
                experiments.add(experiment);
            }
        }
        return experiments;
    }

    public List<LevelPackStructure> decodeLevels(AppData data) {
        List<LevelPackStructure> levelPackStructures = new ArrayList<LevelPackStructure>();
        List<ResponsePack> levelPacksObject = data.getLevelPackList();
        List<LevelStructure> levelStructures;
        List<ResponseLevel> levelsObject;
        for (ResponsePack levelPack : levelPacksObject) {
            levelsObject = levelPack.getLevelList();
            levelStructures = new ArrayList<LevelStructure>();
            for (ResponseLevel level : levelsObject) {
                levelStructures.add(new LevelStructure(level.getId().toString(), level.getOrder(), level.getUrl(), level.getProperties(), level.getVersion().toString()));
            }
            Collections.sort(levelStructures);
            levelPackStructures.add(new LevelPackStructure(levelPack.getToken(), levelPack.getOrder().intValue(), levelStructures));
        }
        Collections.sort(levelPackStructures);
        return levelPackStructures;
    }

    public Map<String, Feature> decodeFeatures(AppData data) {
        Map<String, Feature> features = new HashMap<String, Feature>();
        List<ResponseFeature> featuresList = data.getFeatureList();
        if (featuresList != null) {
            Feature feature;
            for (ResponseFeature featureObj : featuresList) {
                feature = new Feature(featureObj.getToken(), featureObj.getData());
                features.put(featureObj.getToken(), feature);
            }
        }
        return features;
    }
}
