package com.ouchadam.bookkeeper.watcher;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ouchadam.bookkeeper.delegate.BookKeeperDelegate;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.domain.ProgressValues;

public class AsyncNotificationWatcher {

    private final DownloadId downloadId;
    private final Downloadable downloadable;
    private final NotificationServiceBroadcaster notificationServiceBroadcaster;

    public AsyncNotificationWatcher(Context context, Downloadable downloadable, DownloadId downloadId) {
        this.downloadable = downloadable;
        this.downloadId = downloadId;
        notificationServiceBroadcaster = new NotificationServiceBroadcaster(context);
    }

    public void startWatching() {
        notificationServiceBroadcaster.onStart(downloadable, downloadId);
    }

    private static class NotificationServiceBroadcaster {

        private final Context context;

        private NotificationServiceBroadcaster(Context context) {
            this.context = context;
        }

        public void onStart(Downloadable downloadable, DownloadId downloadId) {
            Intent intent = new Intent(context, DownloadNotificationService.class);
            intent.setAction(DownloadNotificationService.ACTION_START);
            int notificationId = downloadable.fileName().hashCode();
            intent.putExtra(DownloadNotificationService.NOTIFICATION_ID, notificationId);
            intent.putExtra(DownloadNotificationService.TITLE, downloadable.title());
            intent.putExtra(DownloadNotificationService.DOWNLOAD_ID, downloadId.value());
            context.startService(intent);
        }

    }

    public static class DownloadNotificationService extends Service implements DownloadWatcher {

        public static final String ACTION_START = "start";
        public static final String NOTIFICATION_ID = "notificationId";
        public static final String TITLE = "title";
        public static final String DOWNLOAD_ID = "downloadId";

        private static final int PROGRESS_MAX = 100;
        private static final boolean DETERMINATE_PROGRESS = false;

        private Notification.Builder notification;
        private NotificationManager notificationManager;
        private int notificationId;
        private String title;
        private DownloadId downloadId;

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (intent != null && intent.getAction() != null) {
                init(intent);
            } else {
                Log.e("???", "received a bad intent, stopping service");
                stopSelf();
            }
            return START_STICKY;
        }

        private void init(Intent intent) {
            BookKeeperDelegate bookKeeer = BookKeeperDelegate.getInstance(this);
            notification = new Notification.Builder(this);
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            this.notificationId = getNotificationId(intent);
            this.title = getTitle(intent);
            this.downloadId = new DownloadId(getDownloadId(intent));
            bookKeeer.startListeningForUpdates(downloadId, this);
        }

        private long getDownloadId(Intent intent) {
            return intent.getLongExtra(DOWNLOAD_ID, -1L);
        }

        private int getNotificationId(Intent intent) {
            return intent.getIntExtra(NOTIFICATION_ID, 0);
        }

        private String getTitle(Intent intent) {
            return intent.getStringExtra(TITLE);
        }

        @Override
        public void onUpdate(ProgressValues progressValues) {
            updateNotificationProgress(progressValues.getPercentage(), progressValues.getDownloaded(), progressValues.getTotal());
            notifyManager();
        }

        private void updateNotificationProgress(int percentage, int downloaded, int total) {
            notification.setProgress(PROGRESS_MAX, percentage, DETERMINATE_PROGRESS);
            notification.setContentText(getProgressContentText(downloaded, total));
        }

        protected String getProgressContentText(int downloadedInBits, int totalDownloadedSizeInBits) {
            return "Downloaded " + bitsToMegabytes(downloadedInBits) + " out of " + bitsToMegabytes(totalDownloadedSizeInBits);
        }

        private String bitsToMegabytes(float bits) {
            float i = bits / 1048576f;
            return String.format("%.2f", i) + "mb";
        }

        @Override
        public boolean isWatching(DownloadId downloadId) {
            return this.downloadId.equals(downloadId);
        }

        @Override
        public void onStart() {
            initNotification(title, notification);
            startForeground(notificationId, notification.build());
        }

        private Notification.Builder initNotification(String title, Notification.Builder notification) {
            notification.setSmallIcon(android.R.drawable.stat_notify_sync_noanim);
            notification.setAutoCancel(true);
            notification.setContentTitle(title);
            notification.setOngoing(true);
            notification.setContentText("Starting download...");
            return notification;
        }

        @Override
        public void onStop() {
            handleOnStop();
            notifyManager();
            stopSelf();
        }

        private void handleOnStop() {
            notification.setProgress(0, 0, false);
            notification.setContentText("Download complete");
            notification.setOngoing(false);
        }

        private void notifyManager() {
            notificationManager.notify(notificationId, notification.build());
        }

    }

}
