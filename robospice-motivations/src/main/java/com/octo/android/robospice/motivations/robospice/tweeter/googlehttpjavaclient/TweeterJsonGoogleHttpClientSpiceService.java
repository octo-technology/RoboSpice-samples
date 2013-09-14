package com.octo.android.robospice.motivations.robospice.tweeter.googlehttpjavaclient;

import android.app.Application;

import android.util.Log;
import com.octo.android.robospice.GoogleHttpClientSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileInputStreamObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
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
        try {
            InFileStringObjectPersister inFileStringObjectPersister = new InFileStringObjectPersister( application );
            InFileInputStreamObjectPersister inFileInputStreamObjectPersister = new InFileInputStreamObjectPersister( application );
            JacksonObjectPersisterFactory inJSonFileObjectPersisterFactory = new JacksonObjectPersisterFactory( application );

            inFileStringObjectPersister.setAsyncSaveEnabled( true );
            inFileInputStreamObjectPersister.setAsyncSaveEnabled( true );
            inJSonFileObjectPersisterFactory.setAsyncSaveEnabled( true );

            cacheManager.addPersister( inFileStringObjectPersister );
            cacheManager.addPersister( inFileInputStreamObjectPersister );
            cacheManager.addPersister( inJSonFileObjectPersisterFactory );
        } catch (CacheCreationException e) {
            Log.e("tag", "error");
        }

        return cacheManager;
    }

}
