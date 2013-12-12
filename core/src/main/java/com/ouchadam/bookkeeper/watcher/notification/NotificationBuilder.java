package com.ouchadam.bookkeeper.watcher.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

class NotificationBuilder {

    private static final int PROGRESS_MAX = 100;
    private static final boolean DETERMINATE_PROGRESS = false;

    private final NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    static NotificationBuilder from(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return new NotificationBuilder(notificationManager, builder);
    }

    NotificationBuilder(NotificationManager notificationManager, NotificationCompat.Builder builder) {
        this.notificationManager = notificationManager;
        this.builder = builder;
    }

    public void updateNotificationProgress(NotificationDataHolder notificationDataHolder, int percentage, int downloaded, int total) {
        builder.setContentTitle(getContentTitle(notificationDataHolder));
        builder.setProgress(PROGRESS_MAX, percentage, DETERMINATE_PROGRESS);
        builder.setContentText(getProgressContentText(downloaded, total));
        update(notificationDataHolder);
    }

    private String getProgressContentText(int downloadedInBits, int totalDownloadedSizeInBits) {
        return "Downloaded " + bitsToMegabytes(downloadedInBits) + " out of " + bitsToMegabytes(totalDownloadedSizeInBits);
    }

    private String bitsToMegabytes(float bits) {
        float i = bits / 1048576f;
        return String.format("%.2f", i) + "mb";
    }

    public void update(NotificationDataHolder notificationDataHolder) {
        notifyManager(notificationDataHolder, builder);
    }

    private void notifyManager(NotificationDataHolder notificationDataHolder, NotificationCompat.Builder notification) {
        notificationManager.notify(notificationDataHolder.getOngoingNotificationId(), build(notification));
    }

    private Notification build(NotificationCompat.Builder notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return notification.build();
        }
        return notification.getNotification();
    }

    private String getContentTitle(NotificationDataHolder notificationDataHolder) {
        return notificationDataHolder.getTitle();
    }

    public Notification buildInitial(NotificationDataHolder notificationDataHolder) {
        return build(getInitialNotification(notificationDataHolder));
    }

    private NotificationCompat.Builder getInitialNotification(NotificationDataHolder notificationDataHolder) {
        NotificationCompat.Builder defaultNotification = createDefaultDownloading(builder);
        defaultNotification.setContentTitle(getContentTitle(notificationDataHolder));
        setClickListeners(notificationDataHolder, defaultNotification);
        return defaultNotification;
    }

    private void setClickListeners(NotificationDataHolder notificationDataHolder, NotificationCompat.Builder defaultNotification) {
        if (notificationDataHolder.getOnClick() != null) {
            defaultNotification.setContentIntent(notificationDataHolder.getOnClick());
        }

        if (notificationDataHolder.getOnCancel() != null) {
            defaultNotification.addAction(0, "Cancel", notificationDataHolder.getOnCancel());
        }
    }

    private NotificationCompat.Builder createDefaultDownloading(NotificationCompat.Builder notification) {
        notification.setSmallIcon(android.R.drawable.stat_sys_download);
        notification.setOngoing(true);
        return notification;
    }

    public void setInitial(NotificationDataHolder notificationDataHolder) {
        notifyManager(notificationDataHolder, getInitialNotification(notificationDataHolder));
    }

    public void dismiss(NotificationDataHolder notificationDataHolder) {
        notificationManager.cancel(notificationDataHolder.getOngoingNotificationId());
    }

    public void setDownloaded(NotificationDataHolder data) {
        notificationManager.cancel(data.getOngoingNotificationId());
        notificationManager.notify(data.getDownloadedNotificationId(), createDownloaded(data));
    }

    private Notification createDownloaded(NotificationDataHolder data) {
        builder.setSmallIcon(android.R.drawable.stat_sys_download_done);
        builder.setContentTitle(data.getTitle() + " downloaded");
        builder.setOngoing(false);
        builder.setContentText("");
        builder.setProgress(0, 0, false);
        return build(builder);
    }
}
