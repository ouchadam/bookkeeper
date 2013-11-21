package com.ouchadam.bookkeeper.delegate;

import android.content.Context;

import com.ouchadam.bookkeeper.BookKeeperRestorer;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.progress.KeeperIntentHandler;
import com.ouchadam.bookkeeper.progress.OnAllDownloadsFinished;
import com.ouchadam.bookkeeper.progress.OnDownloadFinishedListener;
import com.ouchadam.bookkeeper.progress.ProgressReceiver;
import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.DownloadWatcherManager;

import java.util.ArrayList;
import java.util.List;

public class BookKeeperDelegate {

    private final DownloadEnqueuer downloadEnqueuer;
    private final DownloadWatcherManager downloadWatcherManager;
    private final IdManager idManager;
    private final ProgressReceiverController progressController;
    private final WatcherServiceStarter watcherService;

    private static BookKeeperDelegate bookKeeper;

    public static BookKeeperDelegate getInstance(Context context) {
        if (bookKeeper == null) {
            bookKeeper = BookKeeperFactory.create(context);
        }
        return bookKeeper;
    }

    BookKeeperDelegate(Context context, DownloadEnqueuer downloadEnqueuer, DownloadWatcherManager downloadWatcherManager, IdManager idManager, WatcherServiceStarter watcherService) {
        this.downloadEnqueuer = downloadEnqueuer;
        this.downloadWatcherManager = downloadWatcherManager;
        this.idManager = idManager;
        this.watcherService = watcherService;
        this.progressController = createProgressController(context, downloadWatcherManager, downloadFinished, allDownloadsFinished);
    }

    private static ProgressReceiverController createProgressController(Context context, DownloadWatcherManager watcherManager, OnDownloadFinishedListener downloadFinished, OnAllDownloadsFinished allDownloadsFinished) {
        KeeperIntentHandler keeperIntentHandler = new KeeperIntentHandler(watcherManager, downloadFinished, allDownloadsFinished);
        return new ProgressReceiverController(context, new ProgressReceiver(keeperIntentHandler));
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
            progressController.unregister();
        }
    };

    public DownloadId start(Downloadable downloadable) {
        long downloadId = downloadEnqueuer.enqueue(downloadable);
        return new DownloadId(downloadId);
    }

    public void startListeningForUpdates(DownloadId downloadId, DownloadWatcher downloadWatcher) {
        ArrayList<DownloadWatcher> downloadWatchers = new ArrayList<DownloadWatcher>();
        downloadWatchers.add(downloadWatcher);
        startListeningForUpdates(downloadId, downloadWatchers);
    }

    public void startListeningForUpdates(DownloadId downloadId, List<DownloadWatcher> downloadWatchers) {
        attachWatchers(downloadWatchers);
        progressController.register();
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

    public void restore(BookKeeperRestorer bookKeeperRestorer) {
        idManager.restore(bookKeeperRestorer);
    }

    public void delete(DownloadId... downloadIds) {
        downloadEnqueuer.remove(downloadIds);
    }
}
