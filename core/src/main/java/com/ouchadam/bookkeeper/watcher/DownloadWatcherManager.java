package com.ouchadam.bookkeeper.watcher;

import com.ouchadam.bookkeeper.progress.ProgressValues;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DownloadWatcherManager {

    private final List<DownloadWatcher> downloadWatchers;

    public DownloadWatcherManager() {
        this(new ArrayList<DownloadWatcher>());
    }

    private DownloadWatcherManager(List<DownloadWatcher> downloadWatchers) {
        this.downloadWatchers = downloadWatchers;
    }

    public void addWatcher(DownloadWatcher downloadWatcher) {
        downloadWatchers.add(downloadWatcher);
    }

    public void onStart(long downloadId) {
        forEachWithId(onStart, downloadId);
    }

    private final ForEachDownloadWatcher onStart = new ForEachDownloadWatcher() {
        @Override
        public void on(DownloadWatcher downloadWatcher) {
            downloadWatcher.onStart();
        }
    };

    public void onUpdate(long downloadId, final ProgressValues progressValues) {
        forEachWithId(new ForEachDownloadWatcher() {
            @Override
            public void on(DownloadWatcher downloadWatcher) {
                downloadWatcher.onUpdate(progressValues);
            }
        },downloadId);
    }

    public void onStop(long downloadId) {
        forEachWithId(onStop, downloadId);
        cleanWatchers(downloadId);
    }

    private final ForEachDownloadWatcher onStop = new ForEachDownloadWatcher() {
        @Override
        public void on(DownloadWatcher downloadWatcher) {
            downloadWatcher.onStop();
        }
    };

    private void forEachWithId(ForEachDownloadWatcher forEach, long downloadId) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            if (downloadWatcher.isWatching(downloadId)) {
                forEach.on(downloadWatcher);
            }
        }
    }

    private void cleanWatchers(long downloadId) {
        Iterator<DownloadWatcher> iterator = downloadWatchers.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isWatching(downloadId)) {
                iterator.remove();
            }
        }
    }

    private interface ForEachDownloadWatcher {
        void on(DownloadWatcher downloadWatcher);
    }

}
