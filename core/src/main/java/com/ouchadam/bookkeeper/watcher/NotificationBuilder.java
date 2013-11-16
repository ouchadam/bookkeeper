package com.ouchadam.bookkeeper.watcher;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;

class NotificationBuilder {

    public static final int NOTIFICATION_ID = 0xAA;

    private final NotificationManager notificationManager;

    NotificationBuilder(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public void notifyManager(Notification.Builder notification) {
        notificationManager.notify(NOTIFICATION_ID, build(notification));
    }

    public Notification build(Notification.Builder notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return notification.build();
        }
        return notification.getNotification();
    }

}
