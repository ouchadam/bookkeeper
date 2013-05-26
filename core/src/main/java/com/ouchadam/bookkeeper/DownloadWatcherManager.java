package com.ouchadam.bookkeeper;

import java.util.Arrays;
import java.util.List;

public class DownloadWatcherManager {

    private final List<DownloadWatcher> downloadWatchers;

    public DownloadWatcherManager(DownloadWatcher[] downloadWatchers) {
        this(Arrays.asList(downloadWatchers));
    }

    public DownloadWatcherManager(List<DownloadWatcher> downloadWatchers) {
        this.downloadWatchers = downloadWatchers;
    }

    public void onUpdate(ProgressValues progressValues) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onUpdate(progressValues);
        }
    }

    public void onStart(Downloadable downloadable) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onStart(downloadable);
        }
    }

    public void onStop() {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onStop();
        }
    }

}
