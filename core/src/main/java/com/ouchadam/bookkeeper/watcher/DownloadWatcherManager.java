package com.ouchadam.bookkeeper.watcher;

import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.ProgressValues;

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

    public void onStart(DownloadId downloadId) {
        forEachWithId(onStart, downloadId);
    }

    private final ForEachDownloadWatcher onStart = new ForEachDownloadWatcher() {
        @Override
        public void on(DownloadWatcher downloadWatcher, Iterator<DownloadWatcher> iterator) {
            downloadWatcher.onStart();
        }
    };

    public void onUpdate(DownloadId downloadId, final ProgressValues progressValues) {
        forEachWithId(new ForEachDownloadWatcher() {
            @Override
            public void on(DownloadWatcher downloadWatcher, Iterator<DownloadWatcher> iterator) {
                downloadWatcher.onUpdate(progressValues);
            }
        },downloadId);
    }

    public void onStop(DownloadId downloadId) {
        forEachWithId(onStop, downloadId);
        forEachWithId(cleanWatchers, downloadId);
    }

    private final ForEachDownloadWatcher onStop = new ForEachDownloadWatcher() {
        @Override
        public void on(DownloadWatcher downloadWatcher, Iterator<DownloadWatcher> iterator) {
            downloadWatcher.onStop();
        }
    };

    private final ForEachDownloadWatcher cleanWatchers = new ForEachDownloadWatcher() {
        @Override
        public void on(DownloadWatcher downloadWatcher, Iterator<DownloadWatcher> iterator) {
            iterator.remove();
        }
    };

    private void forEachWithId(ForEachDownloadWatcher forEach, DownloadId downloadId) {
        Iterator<DownloadWatcher> iterator = downloadWatchers.iterator();
        while (iterator.hasNext()) {
            DownloadWatcher downloadWatcher = iterator.next();
            if (downloadWatcher.isWatching(downloadId)) {
                forEach.on(downloadWatcher, iterator);
            }
        }
    }

    public void clear() {
        downloadWatchers.clear();
    }

    private interface ForEachDownloadWatcher {
        void on(DownloadWatcher downloadWatcher, Iterator<DownloadWatcher> iterator);
    }

}
