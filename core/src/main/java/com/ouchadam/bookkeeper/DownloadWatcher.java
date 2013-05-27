package com.ouchadam.bookkeeper;

import com.ouchadam.bookkeeper.progress.ProgressValues;

public interface DownloadWatcher {
    void onStart(Downloadable downloadable);
    void onUpdate(ProgressValues progressValues);
    void onStop();
}
