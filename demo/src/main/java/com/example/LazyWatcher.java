package com.example;

import com.ouchadam.bookkeeper.watcher.DownloadWatcher;

public interface LazyWatcher {
    DownloadWatcher create(long downloadId, long itemId);
}
