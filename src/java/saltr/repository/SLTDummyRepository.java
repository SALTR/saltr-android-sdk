/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.repository;

import android.content.Context;
import android.content.ContextWrapper;

/**
 * The SLTDummyRepository class represents the dummy repository.
 */
public class SLTDummyRepository extends ContextWrapper implements ISLTRepository {

    private String applicationDirectory;
    private String cacheDirectory;

    /**
     * Class constructor.
     */
    public SLTDummyRepository(Context base) {
        super(base);
        applicationDirectory = getApplicationInfo().dataDir;
        cacheDirectory = getCacheDir().getPath();
    }

    /**
     * Provides an object from storage.
     *
     * @param name The name of the object.
     * @return <code>null</code> value.
     */
    @Override
    public Object getObjectFromStorage(String name) {
        return null;
    }

    /**
     * Provides an object from cache.
     * @param fileName The name of the object.
     * @return <code>null</code> value.
     */
    @Override
    public Object getObjectFromCache(String fileName) {
        return null;
    }

    /**
     * Provides the object's version.
     * @param name The name of the object.
     * @return The empty value.
     */
    @Override
    public String getObjectVersion(String name) {
        return null;
    }

    /**
     * Stores an object.
     * @param name The name of the object.
     * @param object The object to store.
     */
    @Override
    public void saveObject(String name, Object object) {

    }

    /**
     * Caches an object.
     * @param name The name of the object.
     * @param version The version of the object.
     * @param object The object to store.
     */
    @Override
    public void cacheObject(String name, String version, Object object) {

    }

    /**
     * Provides an object from application.
     * @param fileName The name of the object.
     * @return The requested object.
     */

    @Override
    public Object getObjectFromApplication(String fileName) {
        return null;
    }

    private Object getInternal(String path) {
        return null;
    }
}
