package com.octo.android.robospice.sample.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import roboguice.util.temp.Ln;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.octo.android.robospice.notification.SpiceServiceListenerNotificationService;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestStatus;
import com.octo.android.robospice.request.listener.SpiceServiceListener.RequestProcessingContext;
import com.octo.android.robospice.sample.core.monitor.R;

public class SampleMonitorService extends SpiceServiceListenerNotificationService {

    private static final int NOTIFICATION_ID_PROCESSED = 7000;
    private Map<Integer, NotificationCompat.Builder> mapRequestToNotificationBuilder = Collections.synchronizedMap(new HashMap<Integer, NotificationCompat.Builder>());

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Notification onCreateForegroundNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)//
        .setContentTitle("RoboSpice monitor")//
        .setAutoCancel(true)//
        .setSmallIcon(android.R.drawable.ic_menu_camera);

        Notification notification = builder.getNotification();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
            notification.priority = Notification.PRIORITY_MIN;
        }
        return notification;
    }

    @Override
    public SpiceNotification onCreateNotificationForServiceStopped() {
        String message = "Service stopped";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)//
        .setContentTitle(getSpiceServiceClass().getSimpleName())//
        .setContentText(message)//
        .setAutoCancel(true)//
        .setSmallIcon(R.drawable.ic_launcher_robospice);
        return new SpiceNotification(DEFAULT_ROBOSPICE_NOTIFICATION_ID, builder.getNotification());
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestSucceeded(CachedSpiceRequest<?> cachedSpiceRequest, RequestProcessingContext requestProcessingContext) {
        int hashCode = cachedSpiceRequest.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "Request %d success", hashCode);
        Ln.d(message);
        return updateCustomSpiceNotification(hashCode, cachedSpiceRequest, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestCancelled(CachedSpiceRequest<?> cachedSpiceRequest, RequestProcessingContext requestProcessingContext) {
        int hashCode = cachedSpiceRequest.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "Request %d in canceled", hashCode);
        Ln.d(message);
        return updateCustomSpiceNotification(hashCode, cachedSpiceRequest, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestFailed(CachedSpiceRequest<?> cachedSpiceRequest, RequestProcessingContext requestProcessingContext) {
        int hashCode = cachedSpiceRequest.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "Request %d failure", hashCode);
        Ln.d(message);
        return updateCustomSpiceNotification(hashCode, cachedSpiceRequest, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestProgressUpdate(CachedSpiceRequest<?> cachedSpiceRequest, RequestProcessingContext requestProcessingContext) {
        int hashCode = cachedSpiceRequest.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "Request %d in progress", hashCode);
        Ln.d(message);
        return updateCustomSpiceNotification(hashCode, cachedSpiceRequest, message, requestProcessingContext.getRequestProgress());
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestAdded(CachedSpiceRequest<?> cachedSpiceRequest, RequestProcessingContext requestProcessingContext) {
        int hashCode = cachedSpiceRequest.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "Request %d added", hashCode);
        Ln.d(message);
        return updateCustomSpiceNotification(hashCode, cachedSpiceRequest, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestAggregated(CachedSpiceRequest<?> cachedSpiceRequest, RequestProcessingContext requestProcessingContext) {
        int hashCode = cachedSpiceRequest.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "Request %d aggregated", hashCode);
        Ln.d(message);
        return updateCustomSpiceNotification(hashCode, cachedSpiceRequest, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestNotFound(CachedSpiceRequest<?> cachedSpiceRequest, RequestProcessingContext requestProcessingContext) {
        int hashCode = cachedSpiceRequest.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "Request %d notfound", hashCode);
        Ln.d(message);
        return updateCustomSpiceNotification(hashCode, cachedSpiceRequest, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestProcessed(CachedSpiceRequest<?> cachedSpiceRequest, RequestProcessingContext requestProcessingContext) {
        int hashCode = cachedSpiceRequest.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "Request %d processed", hashCode);
        Ln.d(message);
        return updateCustomSpiceNotification(NOTIFICATION_ID_PROCESSED, cachedSpiceRequest, message);
    }

    private SpiceNotification createCustomSpiceNotification(int id, CachedSpiceRequest<?> cachedSpiceRequest, String text) {
        Ln.d("Create notification " + id);

        Intent intent = new Intent(SampleMonitorService.this, SampleSpiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(SampleMonitorService.this, 06, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)//
        .setContentTitle(getSpiceServiceClass().getSimpleName())//
        .setContentIntent(pendingIntent)//
        .setContentText(text)//
        .setAutoCancel(true)//
        .setSmallIcon(R.drawable.ic_launcher_robospice);

        mapRequestToNotificationBuilder.put(cachedSpiceRequest.hashCode(), builder);
        return new SpiceNotification(id, builder.getNotification());
    }
    
    private SpiceNotification updateCustomSpiceNotification(int id, CachedSpiceRequest<?> cachedSpiceRequest, String text, RequestProgress progress) {
        String progressMessage = text;
        if( progress.getStatus() == RequestStatus.LOADING_FROM_NETWORK) {
            progressMessage += String.format(Locale.US,"(%02d%%)", (int)(100*progress.getProgress()));
        }
        return updateCustomSpiceNotification(id, cachedSpiceRequest, progressMessage); 
    }

    private SpiceNotification updateCustomSpiceNotification(int id, CachedSpiceRequest<?> cachedSpiceRequest, String text) {
        NotificationCompat.Builder builder = mapRequestToNotificationBuilder.get(cachedSpiceRequest.hashCode());
        if( builder == null ) {
            createCustomSpiceNotification(id, cachedSpiceRequest, text);
            builder = mapRequestToNotificationBuilder.get(cachedSpiceRequest.hashCode());
        } else {
            builder.setContentText(text);
            Ln.d("Updated notification " + id);
        }
        return new SpiceNotification(id, builder.getNotification());
    }

}
