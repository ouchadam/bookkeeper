package com.ouchadam.bookkeeper.watcher.notification;

import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.ProgressValues;

class InnerDownloadWatcher implements DownloadWatcher {

    private final DownloadTypeFetcher downloadTypeFetcher;
    private final NotificationDataHolder notificationDataHolder;
    private final OnFinishCallback onFinishCallback;
    private final NotificationBuilder notificationBuilder;

    public interface OnFinishCallback {
        void onFinish(NotificationDataHolder notificationDataHolder);
    }

    InnerDownloadWatcher(DownloadTypeFetcher downloadTypeFetcher, NotificationDataHolder notificationDataHolder, OnFinishCallback onFinishCallback, NotificationBuilder notificationBuilder) {
        this.downloadTypeFetcher = downloadTypeFetcher;
        this.notificationDataHolder = notificationDataHolder;
        this.onFinishCallback = onFinishCallback;
        this.notificationBuilder = notificationBuilder;
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
            notificationBuilder.updateNotificationProgress(progressValues.getPercentage(), progressValues.getDownloaded(), progressValues.getTotal());
        }
    }

    @Override
    public void onStop() {
        onFinishCallback.onFinish(notificationDataHolder);
    }

}
