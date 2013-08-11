package com.example;

import com.ouchadam.bookkeeper.Downloadable;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;

public interface Downloader {
    void watch(long downloadId, DownloadWatcher... downloadWatcher);
    void store(long downloadId, long itemId);
    long keep(Downloadable downloadable);
    void restore(LazyWatcher lazyWatcher);
}
