package com.ouchadam.bookkeeper.watcher.notification;

import android.app.PendingIntent;
import android.content.Intent;

import com.ouchadam.bookkeeper.domain.DownloadId;

class NotificationDataHolder {

    private final String title;
    private final DownloadId downloadId;
    private final PendingIntent onClick;
    private final PendingIntent onCancel;

    public NotificationDataHolder(String title, DownloadId downloadId, PendingIntent onClick, PendingIntent onCancel) {
        this.title = title;
        this.downloadId = downloadId;
        this.onClick = onClick;
        this.onCancel = onCancel;
    }

    public boolean isWatching(DownloadId downloadId) {
        return this.downloadId.equals(downloadId);
    }

    public static NotificationDataHolder from(Intent intent) {
        String title = getTitleFrom(intent);
        DownloadId downloadId = new DownloadId(getDownloadIdFrom(intent));
        PendingIntent onClick = getPendingIntentFrom(intent, DownloadNotificationServiceState.ON_CLICK);
        PendingIntent onCancel = getPendingIntentFrom(intent, DownloadNotificationServiceState.ON_CANCEL);
        return new NotificationDataHolder(title, downloadId, onClick, onCancel);
    }

    private static PendingIntent getPendingIntentFrom(Intent intent, String extra) {
        return intent.getParcelableExtra(extra);
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

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationDataHolder that = (NotificationDataHolder) o;

        if (downloadId != null ? !downloadId.equals(that.downloadId) : that.downloadId != null)
            return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (downloadId != null ? downloadId.hashCode() : 0);
        return result;
    }

    public int getOngoingNotificationId() {
        return hashCode();
    }

    public int getDownloadedNotificationId() {
        return (int) downloadId.value();
    }

    public PendingIntent getOnClick() {
        return onClick;
    }

    public PendingIntent getOnCancel() {
        return onCancel;
    }
}
