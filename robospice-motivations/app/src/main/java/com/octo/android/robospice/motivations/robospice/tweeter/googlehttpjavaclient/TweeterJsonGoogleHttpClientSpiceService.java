package com.octo.android.robospice.motivations.robospice.tweeter.googlehttpjavaclient;

import android.app.Application;

import android.util.Log;
import com.octo.android.robospice.GoogleHttpClientSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileInputStreamObjectPersister;
import com.octo.android.robospice.persistence.googlehttpclient.json.JacksonObjectPersisterFactory;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;

import java.lang.Exception;

public class TweeterJsonGoogleHttpClientSpiceService extends GoogleHttpClientSpiceService {

    @Override
    public int getThreadCount() {
        return 3;
    }

    @Override
    public CacheManager createCacheManager( Application application ) {
        CacheManager cacheManager = new CacheManager();

        // init
        InFileStringObjectPersister inFileStringObjectPersister = null;
        InFileInputStreamObjectPersister inFileInputStreamObjectPersister = null;
        JacksonObjectPersisterFactory inJSonFileObjectPersisterFactory = null;
        try {
            inFileStringObjectPersister = new InFileStringObjectPersister( application );
            inFileInputStreamObjectPersister = new InFileInputStreamObjectPersister( application );
            inJSonFileObjectPersisterFactory = new JacksonObjectPersisterFactory( application );
        } catch (CacheCreationException e) {
            Log.e("tag", "error");
        }

        inFileStringObjectPersister.setAsyncSaveEnabled( true );
        inFileInputStreamObjectPersister.setAsyncSaveEnabled( true );
        inJSonFileObjectPersisterFactory.setAsyncSaveEnabled( true );

        cacheManager.addPersister( inFileStringObjectPersister );
        cacheManager.addPersister( inFileInputStreamObjectPersister );
        cacheManager.addPersister( inJSonFileObjectPersisterFactory );
        return cacheManager;
    }

}
