package com.ouchadam.bookkeeper.watcher.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.ouchadam.bookkeeper.delegate.BookKeeperDelegate;

class FooFactory implements DownloadTypeFetcher {

    private final WatchCounter watchCounter;
    private final ServiceStateExposer serviceStateExposer;

    private BookKeeperDelegate bookKeeper;
    private Notification.Builder notification;
    private NotificationBuilder notificationBuilder;


    public FooFactory(ServiceStateExposer serviceStateExposer) {
        this.watchCounter = new WatchCounter();
        this.serviceStateExposer = serviceStateExposer;
    }

    public void lazyInit(Context context) {
        if (bookKeeper == null) {
            bookKeeper = BookKeeperDelegate.getInstance(context);
            notification = new Notification.Builder(context);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationBuilder = new NotificationBuilder(notificationManager);
        }
    }

    public void handle(Intent intent) {
        NotificationDataHolder notificationDataHolder = NotificationDataHolder.from(intent, notificationBuilder, notification);
        watchCounter.add(notificationDataHolder);
        bookKeeper.startListeningForUpdates(notificationDataHolder.getDownloadId(), new InnerDownloadWatcher(this, notificationDataHolder, onFinishCallback));
        serviceStateExposer.onStartForeground(NotificationBuilder.NOTIFICATION_ID, getInitialNotification(notificationDataHolder));
    }

    private Notification getInitialNotification(NotificationDataHolder notificationDataHolder) {
        return notificationBuilder.build(createInitialNotification(notification, notificationDataHolder));
    }

    private Notification.Builder createInitialNotification(Notification.Builder notification, NotificationDataHolder notificationDataHolder) {
        notification = createDefault(notification);
        notification.setContentTitle(getContentTitle(notificationDataHolder));
        return notification;
    }

    private Notification.Builder createDefault(Notification.Builder notification) {
        notification.setSmallIcon(android.R.drawable.stat_notify_sync_noanim);
        notification.setOngoing(true);
        notification.setContentText(getContentText());
        notification.setProgress(0, 0, !watchCounter.isSingle());
        return notification;
    }

    private String getContentText() {
        if (watchCounter.isSingle()) {
            return "Downloading...";
        }
        return "";
    }

    private String getContentTitle(NotificationDataHolder notificationDataHolder) {
        if (watchCounter.isSingle()) {
            return notificationDataHolder.getTitle();
        }
        return watchCounter.getCount() + " downloads in progress";
    }

    private final InnerDownloadWatcher.OnFinishCallback onFinishCallback = new InnerDownloadWatcher.OnFinishCallback() {
        @Override
        public void onFinish(NotificationDataHolder notificationDataHolder) {
            watchCounter.remove(notificationDataHolder);
            handleFinish();
        }
    };

    public void handleFinish() {
        if (watchCounter.isEmpty()) {
            serviceStateExposer.onStopForeground(false);
            serviceStateExposer.onStopService();
        } else if (watchCounter.isSingle()) {
            notificationBuilder.notifyManager(createInitialNotification(notification, watchCounter.getLastHolder()));
        } else {
            notificationBuilder.notifyManager(getInitialNotification());
        }
    }

    private Notification.Builder getInitialNotification() {
        Notification.Builder defaultNotification = createDefault(notification);
        defaultNotification.setContentTitle(watchCounter.getCount() + " downloads in progress");
        return defaultNotification;
    }

    @Override
    public WatchCounter.DownloadType fetchDownloadType() {
        return watchCounter.getType();
    }

}
