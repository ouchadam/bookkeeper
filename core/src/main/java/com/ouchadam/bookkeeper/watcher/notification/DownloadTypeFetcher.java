package com.ouchadam.bookkeeper.watcher.notification;

import com.ouchadam.bookkeeper.watcher.notification.WatchCounter;

public interface DownloadTypeFetcher {
    WatchCounter.DownloadType fetchDownloadType();
}
