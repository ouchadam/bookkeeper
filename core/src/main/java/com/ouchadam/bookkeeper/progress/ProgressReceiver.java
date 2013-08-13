package com.ouchadam.bookkeeper.progress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.ouchadam.bookkeeper.DownloadId;
import com.ouchadam.bookkeeper.watcher.DownloadWatcherManager;

public class ProgressReceiver extends BroadcastReceiver {

    private final DownloadWatcherManager watcherManager;
    private final OnDownloadFinishedListener downloadFinishedListener;
    private final OnAllDownloadsFinished onAllDownloadsFinished;

    public ProgressReceiver(DownloadWatcherManager watcherManager, OnDownloadFinishedListener downloadFinishedListener, OnAllDownloadsFinished onAllDownloadsFinished) {
        this.watcherManager = watcherManager;
        this.downloadFinishedListener = downloadFinishedListener;
        this.onAllDownloadsFinished = onAllDownloadsFinished;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ProgressUpdater.Action action = ProgressUpdater.Action.valueOf(intent.getAction());
        DownloadId downloadId = new DownloadId(intent.getLongExtra("test3", -1l));
        handleIntent(downloadId, intent, action);
    }

    private void handleIntent(DownloadId downloadId, Intent intent, ProgressUpdater.Action action) {
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

    public void register(Context context) {
        IntentFilter intentFilter = getIntentFilter();
        context.registerReceiver(this, intentFilter);
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        for (ProgressUpdater.Action action : ProgressUpdater.Action.values()) {
            intentFilter.addAction(action.name());
        }
        return intentFilter;
    }

    public void unregister(Context context) {
        try {
            context.unregisterReceiver(this);
        } catch (IllegalArgumentException e) {
            // TODO : receiver has already been unregistered
            e.printStackTrace();
        }
    }

}
