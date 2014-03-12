/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import saltr.parser.response.*;

import java.util.*;

public class SLTDeserializer {

    public List<SLTExperiment> decodeExperiment(AppData data) {
        List<SLTExperiment> experiments = new ArrayList<SLTExperiment>();

        if (data.getExperiment() != null) {
            for (ResponseExperiment item : data.getExperiment()) {
                SLTExperiment experiment = new SLTExperiment();
                experiment.setToken(item.getToken());
                experiment.setPartition(item.getPartitionName());
                experiment.setType(item.getType());
                experiment.setCustomEvents(item.getCustomEventList());
                experiments.add(experiment);
            }
        }
        return experiments;
    }

    public List<SLTLevelPack> decodeLevels(AppData data) {
        List<SLTLevelPack> SLTLevelPacks = new ArrayList<SLTLevelPack>();
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
            SLTLevelPacks.add(new SLTLevelPack(levelPack.getToken(), levelPack.getOrder().intValue(), levelStructures));
        }
        Collections.sort(SLTLevelPacks);
        return SLTLevelPacks;
    }

    public Map<String, SLTFeature> decodeFeatures(AppData data) {
        Map<String, SLTFeature> features = new HashMap<String, SLTFeature>();
        List<ResponseFeature> featuresList = data.getFeatureList();
        if (featuresList != null) {
            SLTFeature feature;
            for (ResponseFeature featureObj : featuresList) {
                feature = new SLTFeature(featureObj.getToken(), featureObj.getData());
                features.put(featureObj.getToken(), feature);
            }
        }
        return features;
    }
}
