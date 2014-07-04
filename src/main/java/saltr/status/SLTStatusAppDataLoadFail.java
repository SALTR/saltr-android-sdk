/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.status;

public class SLTStatusAppDataLoadFail extends SLTStatus {
    public SLTStatusAppDataLoadFail() {
        super(CLIENT_APP_DATA_LOAD_FAIL, "[SALTR] Failed to load appData.");
    }
}
