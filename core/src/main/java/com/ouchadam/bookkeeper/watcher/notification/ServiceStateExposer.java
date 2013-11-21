package com.ouchadam.bookkeeper.watcher.notification;

import android.app.Notification;

public interface ServiceStateExposer {
    void onStartForeground(int notificationId, Notification notification);
    void onStopForeground(boolean removeNotification);
    void onStopService();
}
