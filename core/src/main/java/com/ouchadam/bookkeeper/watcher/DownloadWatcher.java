package com.ouchadam.bookkeeper.watcher;

import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.ProgressValues;

public interface DownloadWatcher {
    boolean isWatching(DownloadId downloadId);
    void onStart();
    void onUpdate(ProgressValues progressValues);
    void onStop();
}
