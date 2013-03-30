package com.octo.android.robospice.sample.service;

import roboguice.util.temp.Ln;
import android.util.Log;
import android.app.Application;

import com.octo.android.robospice.XmlSpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileBitmapObjectPersister;
import com.octo.android.robospice.persistence.memory.LruCacheBitmapObjectPersister;

public class FlickrSpiceService extends XmlSpringAndroidSpiceService {

    @Override
    public void onCreate() {
        super.onCreate();

        // Logging really causes the app to chug with this many requests
        Ln.getConfig().setLoggingLevel(Log.ERROR);
    }

    @Override
    public CacheManager createCacheManager(Application application) {
        CacheManager manager = new CacheManager();

        InFileBitmapObjectPersister filePersister = new InFileBitmapObjectPersister(
            getApplication());
        LruCacheBitmapObjectPersister memoryPersister = new LruCacheBitmapObjectPersister(
            filePersister, 1024 * 1024);

        manager.addPersister(memoryPersister);

        return manager;

    }

    @Override
    public int getThreadCount() {
        return 8;
    }
}
