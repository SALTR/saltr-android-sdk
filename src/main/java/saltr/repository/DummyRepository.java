/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.repository;

public class DummyRepository implements IRepository {
    @Override
    public Object getObjectFromStorage(String name) {
        return null;
    }

    @Override
    public Object getObjectFromCache(String fileName) {
        return null;
    }

    @Override
    public String getObjectVersion(String name) {
        return null;
    }

    @Override
    public void saveObject(String name, Object object) {

    }

    @Override
    public void cacheObject(String name, String version, Object object) {

    }

    @Override
    public Object getObjectFromApplication(String fileName) {
        return null;
    }
}
