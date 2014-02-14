/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr;

public interface SaltrObserver {
    public void onGetAppDataSuccess(ObservableSaltr saltr);
    public void onGetAppDataFail(ObservableSaltr saltr);
    public void onGetLevelDataBodySuccess(ObservableSaltr saltr);
    public void onGetLevelDataBodyFail(ObservableSaltr saltr);
    public void onSaveOrUpdateFeatureSuccess(ObservableSaltr saltr);
    public void onSaveOrUpdateFeatureFail(ObservableSaltr saltr);
}
