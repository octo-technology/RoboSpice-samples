package com.octo.android.robospice.sample.core;

import java.util.Locale;

import roboguice.util.temp.Ln;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.octo.android.robospice.notification.SpiceServiceListenerNotificationService;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.sample.core.monitor.R;

public class SampleMonitorService extends SpiceServiceListenerNotificationService {

    private static final int NOTIFICATION_ID_STOPPED = 7000;
    private static final int NOTIFICATION_ID_PROCESSED = 7010;

    @Override
    public SpiceNotification onCreateNotificationForServiceStopped() {
        String message = "Service stopped";
        Ln.d(message);
        return createCustomSpiceNotification(NOTIFICATION_ID_STOPPED, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestSucceeded(CachedSpiceRequest<?> request, Thread thread) {
        int hashCode = request.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "cachedSpiceRequest %d success", hashCode);
        Ln.d(message);
        return createCustomSpiceNotification(hashCode, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestCancelled(CachedSpiceRequest<?> request, Thread thread) {
        int hashCode = request.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "cachedSpiceRequest %d in canceled", hashCode);
        Ln.d(message);
        return createCustomSpiceNotification(hashCode, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestFailed(CachedSpiceRequest<?> request, Thread thread) {
        int hashCode = request.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "cachedSpiceRequest %d failure", hashCode);
        Ln.d(message);
        return createCustomSpiceNotification(hashCode, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestProgressUpdate(CachedSpiceRequest<?> request, Thread thread) {
        int hashCode = request.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "cachedSpiceRequest %d in progress", hashCode);
        Ln.d(message);
        return createCustomSpiceNotification(hashCode, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestAdded(CachedSpiceRequest<?> request, Thread thread) {
        int hashCode = request.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "cachedSpiceRequest %d added", hashCode);
        Ln.d(message);
        return createCustomSpiceNotification(hashCode, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestNotFound(CachedSpiceRequest<?> request, Thread thread) {
        int hashCode = request.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "cachedSpiceRequest %d notfound", hashCode);
        Ln.d(message);
        return createCustomSpiceNotification(hashCode, message);
    }

    @Override
    public SpiceNotification onCreateNotificationForRequestProcessed(CachedSpiceRequest<?> cachedSpiceRequest) {
        int hashCode = cachedSpiceRequest.getRequestCacheKey().hashCode();
        String message = String.format(Locale.US, "cachedSpiceRequest %d processed", hashCode);
        Ln.d(message);
        return createCustomSpiceNotification(NOTIFICATION_ID_PROCESSED, message);
    }

    private SpiceNotification createCustomSpiceNotification(int id, String text) {
        Ln.d("Create notification " + id);

        Intent intent = new Intent(SampleMonitorService.this, SampleSpiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(SampleMonitorService.this, 06, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)//
                .setContentTitle(text)//
                .setContentIntent(pendingIntent)//
                .setContentText(text)//
                .setTicker(text)//
                .setWhen(System.currentTimeMillis())//
                .setAutoCancel(true)//
                .setSmallIcon(R.drawable.ic_launcher_robospice);

        return new SpiceNotification(id, builder.getNotification());
    }
}
