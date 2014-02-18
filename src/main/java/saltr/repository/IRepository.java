package saltr.repository;

/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
public interface IRepository {
    Object getObjectFromStorage(String name);

    Object getObjectFromCache(String fileName);

    Object getObjectVersion(String name);

    void saveObject(String name, Object object);

    void cacheObject(String name, String version, Object object);

    Object getObjectFromApplication(String fileName);
}
