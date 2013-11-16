package com.ouchadam.bookkeeper.watcher;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;

public class AsyncNotificationWatcher {

    private final DownloadId downloadId;
    private final Downloadable downloadable;
    private final NotificationServiceBroadcaster notificationServiceBroadcaster;

    public AsyncNotificationWatcher(Context context, Downloadable downloadable, DownloadId downloadId) {
        this.downloadable = downloadable;
        this.downloadId = downloadId;
        notificationServiceBroadcaster = new NotificationServiceBroadcaster(context);
    }

    public void startWatching() {
        notificationServiceBroadcaster.onStart(downloadable, downloadId);
    }

    private static class NotificationServiceBroadcaster {

        private final Context context;

        private NotificationServiceBroadcaster(Context context) {
            this.context = context;
        }

        public void onStart(Downloadable downloadable, DownloadId downloadId) {
            Intent intent = new Intent(context, DownloadNotificationService.class);
            intent.setAction(DownloadNotificationService.ACTION_START);
            intent.putExtra(DownloadNotificationService.TITLE, downloadable.title());
            intent.putExtra(DownloadNotificationService.DOWNLOAD_ID, downloadId.value());
            context.startService(intent);
        }

    }

}
