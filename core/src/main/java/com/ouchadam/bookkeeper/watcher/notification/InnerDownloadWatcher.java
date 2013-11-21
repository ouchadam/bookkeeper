package com.ouchadam.bookkeeper.watcher.notification;

import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.ProgressValues;

class InnerDownloadWatcher implements DownloadWatcher {

    private final FooFactory.NotificationItem notificationItem;
    private final OnFinishCallback onFinishCallback;

    public interface OnFinishCallback {
        void onFinish(FooFactory.NotificationItem notificationItem);
    }

    InnerDownloadWatcher(FooFactory.NotificationItem notificationItem, OnFinishCallback onFinishCallback) {
        this.notificationItem = notificationItem;
        this.onFinishCallback = onFinishCallback;
    }

    @Override
    public boolean isWatching(DownloadId downloadId) {
        return this.notificationItem.isWatching(downloadId);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onUpdate(ProgressValues progressValues) {
        notificationItem.updateNotificationProgress(progressValues.getPercentage(), progressValues.getDownloaded(), progressValues.getTotal());
    }

    @Override
    public void onStop() {
        onFinishCallback.onFinish(notificationItem);
    }

}
