package com.ouchadam.bookkeeper.watcher;

import com.ouchadam.bookkeeper.DownloadId;

public interface LazyWatcher {
    DownloadWatcher create(DownloadId downloadId, long itemId);
}
