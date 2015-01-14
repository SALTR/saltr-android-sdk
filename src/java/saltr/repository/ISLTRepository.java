/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr.repository;

/**
 * The ISLTRepository class represents the interface for working with repository.
 */
public interface ISLTRepository {
    /**
     * Provides an object from storage.
     *
     * @param name The name of the object.
     * @return The requested object.
     */
    Object getObjectFromStorage(String name);

    /**
     * Provides an object from cache.
     * @param fileName The name of the object.
     * @return The requested object.
     */
    Object getObjectFromCache(String fileName);

    /**
     * Provides the object's version.
     * @param name The name of the object.
     * @return The version of the requested object.
     */
    String getObjectVersion(String name);

    /**
     * Stores an object.
     * @param name The name of the object.
     * @param object The object to store.
     */
    void saveObject(String name, Object object);

    /**
     * Caches an object.
     * @param name The name of the object.
     * @param version The version of the object.
     * @param object The object to store.
     */
    void cacheObject(String name, String version, Object object);

    /**
     * Provides an object from application.
     * @param fileName The name of the object.
     * @return The requested object.
     */
    Object getObjectFromApplication(String fileName);
}
