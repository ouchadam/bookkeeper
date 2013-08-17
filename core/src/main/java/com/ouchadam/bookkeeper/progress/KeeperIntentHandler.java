package com.ouchadam.bookkeeper.progress;

import android.content.Intent;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.ProgressValues;
import com.ouchadam.bookkeeper.watcher.DownloadWatcherManager;

public class KeeperIntentHandler {

    private final DownloadWatcherManager watcherManager;
    private final OnDownloadFinishedListener downloadFinishedListener;
    private final OnAllDownloadsFinished onAllDownloadsFinished;

    public KeeperIntentHandler(DownloadWatcherManager watcherManager, OnDownloadFinishedListener downloadFinishedListener, OnAllDownloadsFinished onAllDownloadsFinished) {
        this.watcherManager = watcherManager;
        this.downloadFinishedListener = downloadFinishedListener;
        this.onAllDownloadsFinished = onAllDownloadsFinished;
    }

    public void handleIntent(DownloadId downloadId, Intent intent, ProgressUpdater.Action action) {
        switch (action) {
            case UPDATE:
                handleUpdate(downloadId, intent);
                break;
            case STOP:
                handleStop(downloadId);
                downloadFinishedListener.onFinish(downloadId);
                break;
            case ALL_DOWNLOADS_FINISHED:
                handleAllDownloadsFinished();
                onAllDownloadsFinished.onAllFinished();
                break;
            default:
                break;
        }
    }

    private void handleUpdate(DownloadId downloadId, Intent intent) {
        ProgressValues values = (ProgressValues) intent.getSerializableExtra(ProgressUpdater.PROGRESS_VALUES);
        watcherManager.onUpdate(downloadId, values);
    }

    private void handleStop(DownloadId downloadId) {
        watcherManager.onStop(downloadId);
    }

    private void handleAllDownloadsFinished() {
        watcherManager.clear();
    }

}
