package com.ouchadam.downloader;

public interface DownloadWatcher {
    void onStart(Downloadable downloadable);
    void onUpdate(ProgressValues progressValues);
    void onStop();
}
