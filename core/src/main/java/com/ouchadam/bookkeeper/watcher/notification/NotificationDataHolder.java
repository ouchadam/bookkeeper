package com.ouchadam.bookkeeper.watcher.notification;

import android.app.Notification;
import android.content.Intent;

import com.ouchadam.bookkeeper.domain.DownloadId;

class NotificationDataHolder {

    private final String title;
    private final DownloadId downloadId;
    private final NotificationBuilder notificationBuilder;
    private final Notification.Builder notification;

    public NotificationDataHolder(String title, DownloadId downloadId, NotificationBuilder notificationBuilder, Notification.Builder notification) {
        this.title = title;
        this.downloadId = downloadId;
        this.notificationBuilder = notificationBuilder;
        this.notification = notification;
    }

    public boolean isWatching(DownloadId downloadId) {
        return this.downloadId.equals(downloadId);
    }

    public static NotificationDataHolder from(Intent intent, NotificationBuilder notificationBuilder, Notification.Builder notification) {
        String title = getTitleFrom(intent);
        DownloadId downloadId = new DownloadId(getDownloadIdFrom(intent));
        return new NotificationDataHolder(title, downloadId, notificationBuilder, notification);
    }

    private static String getTitleFrom(Intent intent) {
        return intent.getStringExtra(DownloadNotificationServiceState.TITLE);
    }

    private static long getDownloadIdFrom(Intent intent) {
        return intent.getLongExtra(DownloadNotificationServiceState.DOWNLOAD_ID, -1L);
    }

    public DownloadId getDownloadId() {
        return downloadId;
    }

    public void updateNotification() {
        notificationBuilder.notifyManager(notification);
    }

    public String getTitle() {
        return title;
    }

    public Notification.Builder getNotification() {
        return notification;
    }
}
