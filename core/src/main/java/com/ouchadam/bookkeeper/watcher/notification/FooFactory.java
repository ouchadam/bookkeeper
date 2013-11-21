package com.ouchadam.bookkeeper.watcher.notification;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ouchadam.bookkeeper.delegate.BookKeeperDelegate;

class FooFactory implements DownloadTypeFetcher {

    private final WatchCounter watchCounter;
    private final ServiceStateExposer serviceStateExposer;

    private BookKeeperDelegate bookKeeper;
    private NotificationBuilder notificationBuilder;

    public FooFactory(ServiceStateExposer serviceStateExposer) {
        this.watchCounter = new WatchCounter();
        this.serviceStateExposer = serviceStateExposer;
    }

    public void lazyInit(Context context) {
        if (bookKeeper == null) {
            bookKeeper = BookKeeperDelegate.getInstance(context);
            notificationBuilder = NotificationBuilder.from(context);
        }
    }

    public void handle(Intent intent) {
        NotificationDataHolder notificationDataHolder = NotificationDataHolder.from(intent);
        watchCounter.add(NotificationDataHolder.from(intent));
        bookKeeper.startListeningForUpdates(notificationDataHolder.getDownloadId(), new InnerDownloadWatcher(this, notificationDataHolder, onFinishCallback, notificationBuilder));
        serviceStateExposer.onStartForeground(NotificationBuilder.NOTIFICATION_ID, notificationBuilder.buildInitial(watchCounter));
    }

    private final InnerDownloadWatcher.OnFinishCallback onFinishCallback = new InnerDownloadWatcher.OnFinishCallback() {
        @Override
        public void onFinish(NotificationDataHolder notificationDataHolder) {
            Log.e("!!!", "download finished");
            Log.e("!!!", "watch counter is  at : " + watchCounter.getCount());
            watchCounter.remove(notificationDataHolder);
            Log.e("!!!", "watch counter is at after remove : " + watchCounter.getCount());

            handleFinish();
        }
    };

    public void handleFinish() {
        if (watchCounter.isEmpty()) {
            allDownloadsFinished();
        } else {
            Log.e("!!!", "setting initial");
            notificationBuilder.setInitial(watchCounter);
        }
    }

    private void allDownloadsFinished() {
        Log.e("!!!", "all downloads finished");
        serviceStateExposer.onStopForeground(true);
        serviceStateExposer.onStopService();
    }

    @Override
    public WatchCounter.DownloadType fetchDownloadType() {
        return watchCounter.getType();
    }

}
