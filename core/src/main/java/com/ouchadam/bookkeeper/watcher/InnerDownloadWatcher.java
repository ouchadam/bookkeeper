package com.ouchadam.bookkeeper.watcher;

import android.app.Notification;

import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.ProgressValues;

class InnerDownloadWatcher implements DownloadWatcher {

    private static final int PROGRESS_MAX = 100;
    private static final boolean DETERMINATE_PROGRESS = false;

    private DownloadTypeFetcher downloadTypeFetcher;
    private final NotificationDataHolder notificationDataHolder;
    private final OnFinishCallback onFinishCallback;

    public interface OnFinishCallback {
        void onFinish(NotificationDataHolder notificationDataHolder);
    }

    InnerDownloadWatcher(DownloadTypeFetcher downloadTypeFetcher, NotificationDataHolder notificationDataHolder, OnFinishCallback onFinishCallback) {
        this.downloadTypeFetcher = downloadTypeFetcher;
        this.notificationDataHolder = notificationDataHolder;
        this.onFinishCallback = onFinishCallback;
    }

    @Override
    public boolean isWatching(DownloadId downloadId) {
        return this.notificationDataHolder.isWatching(downloadId);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onUpdate(ProgressValues progressValues) {
        WatchCounter.DownloadType downloadType = downloadTypeFetcher.fetchDownloadType();
        if (downloadType == WatchCounter.DownloadType.SINGLE) {
            updateNotificationProgress(notificationDataHolder.getNotification(), progressValues.getPercentage(), progressValues.getDownloaded(), progressValues.getTotal());
            notificationDataHolder.updateNotification();
        }
    }

    private void updateNotificationProgress(Notification.Builder notification, int percentage, int downloaded, int total) {
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
        onFinishCallback.onFinish(notificationDataHolder);
    }

}
