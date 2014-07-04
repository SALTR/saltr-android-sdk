/**
 * Copyright Teoken LLC. (c) 2013. All rights reserved.
 * Copying or usage of any piece of this source code without written notice from Teoken LLC is a major crime.
 * Այս կոդը Թեոկեն ՍՊԸ ընկերության սեփականությունն է:
 * Առանց գրավոր թույլտվության այս կոդի պատճենահանումը կամ օգտագործումը քրեական հանցագործություն է:
 */
package saltr.repository;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class SLTDummyRepository extends ContextWrapper implements ISLTRepository {

    private String applicationDirectory;
    private String cacheDirectory;

    public SLTDummyRepository(Context base) {
        super(base);
        applicationDirectory = getApplicationInfo().dataDir;
        cacheDirectory = getCacheDir().getPath();
    }


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
        String path = applicationDirectory + File.separator + fileName;
        return getInternal(path);
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
            System.err.println("[MobileStorageEngine] : error while getting object.\n', message : '" + e.getMessage() + "'");
        }
        return object;
    }
}
