package com.ouchadam.bookkeeper;

import com.ouchadam.bookkeeper.progress.ProgressValues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DownloadWatcherManager {

    private List<DownloadWatcher> downloadWatchers;

    public DownloadWatcherManager() {
        this(new ArrayList<DownloadWatcher>());
    }

    private DownloadWatcherManager(List<DownloadWatcher> downloadWatchers) {
        this.downloadWatchers = downloadWatchers;
    }

    public void addWatcher(DownloadWatcher downloadWatcher) {
        downloadWatchers.add(downloadWatcher);
    }

    public void onStart(Downloadable downloadable) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onStart(downloadable);
        }
    }

    public void onUpdate(ProgressValues progressValues) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onUpdate(progressValues);
        }
    }

    public void onStop() {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onStop();
        }
    }

}
