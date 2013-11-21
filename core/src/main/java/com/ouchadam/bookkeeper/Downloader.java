package com.ouchadam.bookkeeper;

import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;

public interface Downloader {
    void watch(DownloadId downloadId, DownloadWatcher... downloadWatcher);
    void store(DownloadId downloadId, long itemId);
    DownloadId keep(Downloadable downloadable);
    void restore(LazyWatcher lazyWatcher);
}
