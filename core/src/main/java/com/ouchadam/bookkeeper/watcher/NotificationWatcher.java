package com.ouchadam.bookkeeper.watcher;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.domain.ProgressValues;

import static android.R.drawable.stat_notify_sync_noanim;

public class NotificationWatcher implements DownloadWatcher {

    private static final int PROGRESS_MAX = 100;
    private static final boolean DETERMINATE_PROGRESS = false;

    private final NotificationManager notificationManager;
    private final int notificationId;
    private final Notification.Builder notification;
    private final Downloadable downloadable;
    private final DownloadId downloadId;

    public NotificationWatcher(Context context, Downloadable downloadable, DownloadId downloadId) {
        this.downloadable = downloadable;
        this.downloadId = downloadId;
        this.notificationId = downloadable.fileName().hashCode();
        notification = new Notification.Builder(context);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public boolean isWatching(DownloadId downloadId) {
        return this.downloadId.equals(downloadId);
    }

    @Override
    public void onStart() {
        initNotification(downloadable, notification);
        notifyManager();
    }

    protected Notification.Builder initNotification(Downloadable downloadable, Notification.Builder notification) {
        notification.setSmallIcon(stat_notify_sync_noanim);
        notification.setAutoCancel(true);
        notification.setContentTitle(downloadable.title());
        notification.setOngoing(true);
        notification.setContentText("Starting download...");
        return notification;
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
    public void onStop() {
        notification.setProgress(0, 0, false);
        notification.setContentText("Download complete");
        notification.setOngoing(false);
        notifyManager();
    }

    private void notifyManager() {
        notificationManager.notify(notificationId, notification.build());
    }

}
