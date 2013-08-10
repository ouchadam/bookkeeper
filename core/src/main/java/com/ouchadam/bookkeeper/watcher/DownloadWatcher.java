package com.ouchadam.bookkeeper.watcher;

import com.ouchadam.bookkeeper.progress.ProgressValues;

public interface DownloadWatcher {
    boolean isWatching(long downloadId);
    void onStart();
    void onUpdate(ProgressValues progressValues);
    void onStop();
}
