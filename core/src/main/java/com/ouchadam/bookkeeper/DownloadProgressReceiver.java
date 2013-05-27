package com.ouchadam.bookkeeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.ouchadam.bookkeeper.bundle.DownloadableBundler;
import com.ouchadam.bookkeeper.bundle.Bundler;

public class DownloadProgressReceiver extends BroadcastReceiver {

    private final DownloadWatcherManager watcherManager;
    private DownloadProgressReceiver.OnDownloadFinishedListener downloadFinishedListener;
    private final Bundler<Downloadable> bundler;

    public interface OnDownloadFinishedListener {
        void onFinish();
    }

    public DownloadProgressReceiver(DownloadWatcherManager watcherManager, OnDownloadFinishedListener downloadFinishedListener) {
        this.watcherManager = watcherManager;
        this.downloadFinishedListener = downloadFinishedListener;
        bundler = new DownloadableBundler();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ProgressUpdater.Action action = ProgressUpdater.Action.valueOf(intent.getAction());
        handleIntent(intent, action);
    }

    private void handleIntent(Intent intent, ProgressUpdater.Action action) {
        switch (action) {
            case UPDATE:
                handleUpdate(intent);
                break;
            case START:
                handleStart(intent);
                break;
            case STOP:
                handleStop();
                downloadFinishedListener.onFinish();
                break;
            default:
                break;
        }
    }

    private void handleStart(Intent intent) {
        Bundle downloadable = intent.getBundleExtra(ProgressUpdater.DOWNLOADABLE);
        watcherManager.onStart(bundler.from(downloadable));
    }

    private void handleUpdate(Intent intent) {
        ProgressValues values = (ProgressValues) intent.getSerializableExtra(ProgressUpdater.PROGRESS_VALUES);
        watcherManager.onUpdate(values);
    }

    private void handleStop() {
        watcherManager.onStop();
    }

    public void unregister(Context context) {
        try {
            context.getApplicationContext().unregisterReceiver(this);
        } catch (IllegalArgumentException e) {
            // TODO : receiver has already been unregistered
            e.printStackTrace();
        }
    }

    public void register(Context context) {
        IntentFilter intentFilter = getIntentFilter();
        context.getApplicationContext().registerReceiver(this, intentFilter);
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        for (ProgressUpdater.Action action : ProgressUpdater.Action.values()) {
            intentFilter.addAction(action.name());
        }
        return intentFilter;
    }

}
