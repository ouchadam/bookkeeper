package com.ouchadam.bookkeeper.watcher;

import android.app.Notification;

public interface ServiceExposer {
    void onStartForeground(int notificationId, Notification notification);
    void onStopForeground(boolean removeNotification);
    void onStopService();
}
