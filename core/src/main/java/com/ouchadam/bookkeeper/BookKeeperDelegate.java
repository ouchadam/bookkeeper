package com.ouchadam.bookkeeper;

import android.content.Context;
import com.ouchadam.bookkeeper.progress.OnAllDownloadsFinished;
import com.ouchadam.bookkeeper.progress.OnDownloadFinishedListener;
import com.ouchadam.bookkeeper.progress.ProgressReceiver;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.DownloadWatcherManager;

import java.util.List;

public class BookKeeperDelegate {

    private final DownloadEnqueuer downloadEnqueuer;
    private final DownloadWatcherManager downloadWatcherManager;
    private final IdManager idManager;
    private final ProgressReceiverRegisterer progressReceiver;
    private final WatcherServiceStarter watcherService;

    public BookKeeperDelegate(Context context, DownloadEnqueuer downloadEnqueuer, DownloadWatcherManager downloadWatcherManager, IdManager idManager, WatcherServiceStarter watcherService) {
        this.downloadEnqueuer = downloadEnqueuer;
        this.downloadWatcherManager = downloadWatcherManager;
        this.idManager = idManager;
        this.watcherService = watcherService;
        this.progressReceiver = new ProgressReceiverRegisterer(context, new ProgressReceiver(downloadWatcherManager, downloadFinished, allDownloadsFinished));
    }

    private final OnDownloadFinishedListener downloadFinished = new OnDownloadFinishedListener() {
        @Override
        public void onFinish(DownloadId downloadId) {
            idManager.onFinish(downloadId);
        }
    };

    private final OnAllDownloadsFinished allDownloadsFinished = new OnAllDownloadsFinished() {
        @Override
        public void onAllFinished() {
            progressReceiver.unregister();
        }
    };

    public DownloadId start(Downloadable downloadable) {
        long downloadId = downloadEnqueuer.start(downloadable);
        return new DownloadId(downloadId);
    }

    public void startListeningForUpdates(DownloadId downloadId, List<DownloadWatcher> downloadWatchers) {
        attachWatchers(downloadWatchers);
        progressReceiver.register();
        broadcastStart(downloadId);
        watcherService.startWatching();
    }

    private void attachWatchers(List<DownloadWatcher> downloadWatchers) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            attachWatcher(downloadWatcher);
        }
    }

    private void attachWatcher(DownloadWatcher downloadWatcher) {
        downloadWatcherManager.addWatcher(downloadWatcher);
    }

    private void broadcastStart(DownloadId downloadId) {
        downloadWatcherManager.onStart(downloadId);
    }

    public void store(DownloadId downloadId, long itemId) {
        idManager.addWithItem(downloadId, itemId);
    }

    public void restore(IdManager.BookKeeperRestorer bookKeeperRestorer) {
        idManager.restore(bookKeeperRestorer);
    }
}
