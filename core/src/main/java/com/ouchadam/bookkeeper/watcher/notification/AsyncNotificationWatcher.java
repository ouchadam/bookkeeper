package com.ouchadam.bookkeeper.watcher.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;

public class AsyncNotificationWatcher {

    private final DownloadId downloadId;
    private final PendingIntent onClick;
    private final PendingIntent onCancel;
    private final Downloadable downloadable;
    private final NotificationServiceBroadcaster notificationServiceBroadcaster;

    public AsyncNotificationWatcher(Context context, Downloadable downloadable, DownloadId downloadId, PendingIntent onClick, PendingIntent onCancel) {
        this.downloadable = downloadable;
        this.downloadId = downloadId;
        this.onClick = onClick;
        this.onCancel = onCancel;
        this.notificationServiceBroadcaster = new NotificationServiceBroadcaster(context);
    }

    public void startWatching() {
        notificationServiceBroadcaster.onStart(downloadable, downloadId, onClick, onCancel);
    }

    private static class NotificationServiceBroadcaster {

        private final Context context;

        private NotificationServiceBroadcaster(Context context) {
            this.context = context;
        }

        public void onStart(Downloadable downloadable, DownloadId downloadId, PendingIntent onClick, PendingIntent onCancel) {
            Intent intent = new Intent(context, DownloadNotificationServiceState.class);
            intent.setAction(DownloadNotificationServiceState.ACTION_START);
            intent.putExtra(DownloadNotificationServiceState.TITLE, downloadable.title());
            intent.putExtra(DownloadNotificationServiceState.DOWNLOAD_ID, downloadId.value());
            intent.putExtra(DownloadNotificationServiceState.ON_CLICK, onClick);
            intent.putExtra(DownloadNotificationServiceState.ON_CANCEL, onCancel);
            context.startService(intent);
        }

    }

}
