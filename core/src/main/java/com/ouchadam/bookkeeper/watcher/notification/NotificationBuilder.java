package com.ouchadam.bookkeeper.watcher.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

class NotificationBuilder {

    public static final int NOTIFICATION_ID = 0xAA;

    private static final int PROGRESS_MAX = 100;
    private static final boolean DETERMINATE_PROGRESS = false;

    private final NotificationManager notificationManager;
    private Notification.Builder builder;

    static NotificationBuilder from(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return new NotificationBuilder(notificationManager, builder);
    }

    NotificationBuilder(NotificationManager notificationManager, Notification.Builder builder) {
        this.notificationManager = notificationManager;
        this.builder = builder;
    }

    public void updateNotificationProgress(int percentage, int downloaded, int total) {
        builder.setProgress(PROGRESS_MAX, percentage, DETERMINATE_PROGRESS);
        builder.setContentText(getProgressContentText(downloaded, total));
        update();
    }

    public void update() {
        notifyManager(builder);
    }

    protected String getProgressContentText(int downloadedInBits, int totalDownloadedSizeInBits) {
        return "Downloaded " + bitsToMegabytes(downloadedInBits) + " out of " + bitsToMegabytes(totalDownloadedSizeInBits);
    }

    private String bitsToMegabytes(float bits) {
        float i = bits / 1048576f;
        return String.format("%.2f", i) + "mb";
    }

    private void notifyManager(Notification.Builder notification) {
        notificationManager.notify(NOTIFICATION_ID, build(notification));
    }

    private Notification build(Notification.Builder notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return notification.build();
        }
        return notification.getNotification();
    }

    private Notification.Builder createDefault(WatchCounter watchCounter, Notification.Builder notification) {
        notification.setSmallIcon(android.R.drawable.stat_notify_sync_noanim);
        notification.setOngoing(true);
        notification.setContentText(getContentText(watchCounter));
        notification.setProgress(0, 0, !watchCounter.isSingle());
        return notification;
    }

    private String getContentText(WatchCounter watchCounter) {
        if (watchCounter.isSingle()) {
            return "Downloading...";
        }
        return "";
    }

    private String getContentTitle(WatchCounter watchCounter, NotificationDataHolder notificationDataHolder) {
        if (watchCounter.isSingle()) {
            return notificationDataHolder.getTitle();
        }
        return watchCounter.getCount() + " downloads in progress";
    }

    public Notification buildInitial(WatchCounter watchCounter) {
        return build(getInitialNotification(watchCounter));
    }

    private Notification.Builder getInitialNotification(WatchCounter watchCounter) {
        Notification.Builder defaultNotification = createDefault(watchCounter, builder);
        defaultNotification.setContentTitle(getContentTitle(watchCounter, watchCounter.getLastHolder()));
        return defaultNotification;
    }

    public void setInitial(WatchCounter watchCounter) {
        notifyManager(getInitialNotification(watchCounter));
    }
}
