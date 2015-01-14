/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.repository;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The SLTRepository class represents the mobile repository.
 */
public class SLTRepository extends ContextWrapper implements ISLTRepository {

    private String applicationDirectory;
    private String cacheDirectory;

    /**
     * Class constructor.
     */
    public SLTRepository(Context base) {
        super(base);
        applicationDirectory = getApplicationInfo().dataDir;
        cacheDirectory = getCacheDir().getPath();
    }

    /**
     * Provides an object from cache.
     *
     * @param name The name of the object.
     * @return The requested object.
     */
    @Override
    public Object getObjectFromCache(String name) {
        return getInternal(cacheDirectory + File.separator + name);
    }

    /**
     * Provides the object's version.
     * @param name The name of the object.
     * @return The version of the requested object.
     */
    @Override
    public String getObjectVersion(String name) {
        String path = cacheDirectory + File.separator + name.replace("", "") + "_VERSION_";
        Object obj = getInternal(path);
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = (Map<String, Object>) obj;
        return map.get("_VERSION_").toString();
    }

    /**
     * Caches an object.
     * @param name The name of the object.
     * @param version The version of the object.
     * @param object The object to store.
     */
    @Override
    public void cacheObject(String name, String version, Object object) {
        String path = cacheDirectory + File.separator + name;
        saveInternal(path, object);
        path = cacheDirectory + File.separator + name.replace("", "") + "_VERSION_";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("_VERSION_", version);
        saveInternal(path, map);
    }

    /**
     * Provides an object from application.
     * @param name The name of the object.
     * @return The requested object.
     */
    @Override
    public Object getObjectFromApplication(String name) {
        String path = applicationDirectory + File.separator + name;
        return getInternal(path);
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
     * Provides an object from storage.
     * @param name The name of the object.
     * @return The requested object.
     */
    @Override
    public Object getObjectFromStorage(String name) {
        return null;
    }

    private Object getInternal(String path) {
        FileInputStream fin;
        Object object = null;
        try {
            fin = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fin);
            object = ois.readObject();

            fin.close();
            ois.close();
        } catch (Exception e) {
            Log.e("SALTR", "[MobileStorageEngine] : error while getting object.\n', message : '" + e.getMessage() + "'");
        }
        return object;
    }

    private void saveInternal(String path, Object objectToSave) {
        try {
            FileOutputStream out = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(objectToSave);

            out.close();
            oos.close();
        } catch (IOException e) {
            Log.e("SALTR", "[MobileStorageEngine] : error while saving object.\n', message : '" + e.getMessage() + "'");
        }
    }
}
