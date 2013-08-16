package com.octo.android.robospice.sample.core;

import android.app.Application;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileBitmapObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;

/**
 * Simple service
 * @author sni
 */
public class SampleSpiceService extends SpiceService {

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();

        // init
        InFileStringObjectPersister inFileStringObjectPersister = new InFileStringObjectPersister(application);
        InFileBitmapObjectPersister inFileBitmapObjectPersister = new InFileBitmapObjectPersister(application);

        cacheManager.addPersister(inFileStringObjectPersister);
        cacheManager.addPersister(inFileBitmapObjectPersister);
        return cacheManager;
    }

    @Override
    public int getThreadCount() {
        return 3;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.v("SampleSpiceService","Starting service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("SampleSpiceService","Starting service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("SampleSpiceService","Stopping service");
    }

    public Notification createDefaultNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(0);
        builder.setTicker(null);
        builder.setWhen(System.currentTimeMillis());
        final Notification note = builder.getNotification();
        if( android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
            note.priority = Notification.PRIORITY_MIN;
        }
        return note;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        Log.v("SampleSpiceService","Bound service");
        return super.onBind(intent);
    }
    
    @Override
    public void onRebind(Intent intent) {
        Log.v("SampleSpiceService","Rebound service");
        super.onRebind(intent);
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        Log.v("SampleSpiceService","Unbound service");
        return super.onUnbind(intent);
    }
}
