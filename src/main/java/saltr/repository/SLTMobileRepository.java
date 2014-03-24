/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.repository;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SLTMobileRepository extends ContextWrapper implements ISLTRepository {

    private String applicationDirectory;
    private String cacheDirectory;

    public SLTMobileRepository(Context base) {
        super(base);
        applicationDirectory = getApplicationInfo().dataDir;
        cacheDirectory = getCacheDir().getPath();
    }

    @Override
    public Object getObjectFromCache(String name) {
        return getInternal(cacheDirectory + File.separator + name);
    }

    @Override
    public String getObjectVersion(String name) {
        String path = cacheDirectory + File.separator + name.replace(".", "") + "_VERSION_";
        Object obj = getInternal(path);
        if (obj == null) {
            return null;
        }
        Map<String, String> map = (Map<String, String>) obj;
        return map.get("_VERSION_");
    }

    @Override
    public void cacheObject(String name, String version, Object object) {
        String path = cacheDirectory + File.separator + name;
        saveInternal(path, object);
        path = cacheDirectory + File.separator + name.replace(".", "") + "_VERSION_";
        Map<String, String> map = new HashMap<>();
        map.put("_VERSION_", version);
        saveInternal(path, map);
    }

    @Override
    public Object getObjectFromApplication(String name) {
        String path = applicationDirectory + File.separator + name;
        return getInternal(path);
    }

    @Override
    public void saveObject(String name, Object object) {

    }

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
            System.out.println("[MobileStorageEngine] : error while getting object.\n', message : '" + e.getMessage() + "'");
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
            System.out.println("[MobileStorageEngine] : error while saving object.\n', message : '" + e.getMessage() + "'");
        }
    }
}
