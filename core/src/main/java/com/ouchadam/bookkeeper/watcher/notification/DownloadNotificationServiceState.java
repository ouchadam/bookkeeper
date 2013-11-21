package com.ouchadam.bookkeeper.watcher.notification;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DownloadNotificationServiceState extends Service implements ServiceStateExposer {

    public static final String ACTION_START = "start";
    public static final String TITLE = "title";
    public static final String DOWNLOAD_ID = "downloadId";

    private final FooFactory fooFactory;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public DownloadNotificationServiceState() {
        fooFactory = new FooFactory(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fooFactory.lazyInit(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("XXX", "onStartCommand");
        if (intent != null && intent.getAction() != null) {
            init(intent);
        } else {
            Log.e("???", "received a bad intent, stopping service");
            stopSelf();
        }
        return START_STICKY;
    }

    private void init(Intent intent) {
        fooFactory.handle(intent);
    }

    @Override
    public void onStartForeground(int notificationId, Notification notification) {
        startForeground(notificationId, notification);
    }

    @Override
    public void onStopForeground(boolean removeNotification) {
        stopForeground(removeNotification);
    }

    @Override
    public void onStopService() {
        stopSelf();
    }

}
