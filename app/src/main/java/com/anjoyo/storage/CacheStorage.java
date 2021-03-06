package com.anjoyo.storage;

import java.io.File;


public class CacheStorage implements Storage {

    String mKey;
    File mFile;

    public CacheStorage(String key) {
        if (Checker.isEmpty(key))
            throw new IllegalArgumentException("cache key cannot be empty");
        mKey = key;
    }

    @Override
    public File getFile() {
        if (mFile == null) {
            mFile = new File(getCacheHome(), mKey);
        }
        mFile.setLastModified(System.currentTimeMillis());
        return mFile;
    }

    protected File getCacheHome() {
        File cacheHome = new File(STORAGE_HOME, ".cache");
        if (!cacheHome.exists()) cacheHome.mkdirs();
        return cacheHome;
    }

}
