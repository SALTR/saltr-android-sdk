/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

import saltr.parser.response.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deserializer {
    private List<Feature> features;
    private List<Experiment> experiments;
    private List<LevelPackStructure> levelPackStructures;

    public Deserializer() {
        features = new ArrayList<Feature>();
        experiments = new ArrayList<Experiment>();
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public List<LevelPackStructure> getLevelPackStructures() {
        return levelPackStructures;
    }

    public void decode(AppData data) {
        if (data == null) {
            return;
        }

        clearData();
        decodeFeatures(data);
        decodeExperimentInfo(data);
        decodeLevels(data);
    }

    private void decodeExperimentInfo(AppData data) {
        if (data.getExperiment() != null) {
            for (ResponseExperiment item: data.getExperiment()) {
                Experiment experiment = new Experiment();
                experiment.setToken(item.getToken());
                experiment.setPartition(item.getPartitionName());
                experiment.setType(item.getType().getValue());
                experiment.setCustomEvents(item.getCustomEventList());
                experiments.add(experiment);
            }
        }
    }

    private void decodeLevels(AppData data) {
        List<ResponsePack> levelPacksObject = data.getLevelPackList();
        levelPackStructures = new ArrayList<LevelPackStructure>();
        List<LevelStructure> levelStructures;
        List<ResponseLevel> levelsObject;
        for (ResponsePack levelPack: levelPacksObject) {
            levelsObject = levelPack.getLevelList();
            levelStructures = new ArrayList<LevelStructure>();
            for (ResponseLevel level: levelsObject) {
                levelStructures.add(new LevelStructure(level.getId().toString(), level.getOrder(), level.getUrl(), level.getProperties(), level.getVersion().toString()));
            }
            Collections.sort(levelStructures);
            levelPackStructures.add(new LevelPackStructure(levelPack.getToken(), levelPack.getOrder().intValue(), levelStructures));
        }
        Collections.sort(levelPackStructures);
    }

    private void decodeFeatures(AppData data) {
        List<ResponseFeature> featuresList = data.getFeatureList();
        if (featuresList != null) {
            Feature feature;
            for (ResponseFeature featureObj: featuresList) {
                feature = new Feature(featureObj.getToken(), featureObj.getData());
                features.add(feature);
            }
        }
    }

    private void clearData() {
        if (features != null) {
            features = new ArrayList<Feature>();
        }
        if (experiments != null) {
            experiments = new ArrayList<Experiment>();
        }
        if (levelPackStructures != null) {
            levelPackStructures = new ArrayList<LevelPackStructure>();
        }
    }
}
