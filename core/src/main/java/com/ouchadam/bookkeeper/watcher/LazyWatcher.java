package com.ouchadam.bookkeeper.watcher;

import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.domain.DownloadId;

public interface LazyWatcher {
    DownloadWatcher create(DownloadId downloadId, long itemId);
}
