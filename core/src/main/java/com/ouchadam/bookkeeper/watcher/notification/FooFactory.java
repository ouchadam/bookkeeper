package com.ouchadam.bookkeeper.watcher.notification;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ouchadam.bookkeeper.delegate.BookKeeperDelegate;
import com.ouchadam.bookkeeper.domain.DownloadId;

class FooFactory implements DownloadTypeFetcher {

    private final WatchCounter watchCounter;
    private final ServiceStateExposer serviceStateExposer;

    private BookKeeperDelegate bookKeeper;
    //    private NotificationBuilder notificationBuilder;
    private boolean isForeground;
    private Context context;

    public FooFactory(ServiceStateExposer serviceStateExposer) {
        this.watchCounter = new WatchCounter();
        this.serviceStateExposer = serviceStateExposer;
        this.isForeground = false;
    }

    public void lazyInit(Context context) {
        this.context = context;
        if (bookKeeper == null) {
            bookKeeper = BookKeeperDelegate.getInstance(context);
        }
    }

    public void handle(Intent intent) {
        NotificationDataHolder notificationDataHolder = NotificationDataHolder.from(intent);
        watchCounter.add(notificationDataHolder);
        NotificationItem notificationItem = new NotificationItem(notificationDataHolder, NotificationBuilder.from(context));


        InnerDownloadWatcher downloadWatcher = new InnerDownloadWatcher(notificationItem, onFinishCallback);
        bookKeeper.startListeningForUpdates(notificationDataHolder.getDownloadId(), downloadWatcher);
        if (!startForeground(notificationItem)) {
            notificationItem.showInitial();
        }
    }

    static class NotificationItem {

        final NotificationDataHolder data;
        final NotificationBuilder builder;

        private NotificationItem(NotificationDataHolder data, NotificationBuilder builder) {
            this.data = data;
            this.builder = builder;
        }

        public void startForeground(ServiceStateExposer serviceStateExposer) {
            serviceStateExposer.onStartForeground(data.notificationId(), builder.buildInitial(data));
        }

        public void showInitial() {
            builder.setInitial(data);
        }

        public boolean isWatching(DownloadId downloadId) {
            return data.isWatching(downloadId);
        }

        public void updateNotificationProgress(int percentage, int downloaded, int total) {
            builder.updateNotificationProgress(data, percentage, downloaded, total);
        }
    }

    private boolean startForeground(NotificationItem notificationItem) {
        if (!isForeground) {
            notificationItem.startForeground(serviceStateExposer);
            this.isForeground = true;
            return true;
        }
        return false;
    }

    private final InnerDownloadWatcher.OnFinishCallback onFinishCallback = new InnerDownloadWatcher.OnFinishCallback() {
        @Override
        public void onFinish(NotificationItem notificationItem) {
            watchCounter.remove(notificationItem.data);
            handleFinish();
        }
    };

    private void handleFinish() {
        if (watchCounter.isEmpty()) {
            allDownloadsFinished();
        } else {
            swapForeground(new NotificationItem(watchCounter.getLastHolder(), NotificationBuilder.from(context)));
        }
    }

    private void allDownloadsFinished() {
        stopForeground();
        serviceStateExposer.onStopService();
    }

    private void swapForeground(NotificationItem notificationItem) {
        notificationItem.startForeground(serviceStateExposer);
    }

    private void stopForeground() {
        serviceStateExposer.onStopForeground(false);
        this.isForeground = false;
    }

    @Override
    public WatchCounter.DownloadType fetchDownloadType() {
        return watchCounter.getType();
    }

}
