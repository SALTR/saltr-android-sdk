/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr.repository;

public interface ISLTRepository {
    Object getObjectFromStorage(String name);

    Object getObjectFromCache(String fileName);

    String getObjectVersion(String name);

    void saveObject(String name, Object object);

    void cacheObject(String name, String version, Object object);

    Object getObjectFromApplication(String fileName);
}
