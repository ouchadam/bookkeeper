package com.ouchadam.downloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.ouchadam.downloader.bundle.Bundler;
import com.ouchadam.downloader.bundle.DownloadableBundler;

public class DownloadProgressReceiver extends BroadcastReceiver {

    private final DownloadWatcherManager watcherManager;
    private final Bundler<Downloadable> bundler;

    public DownloadProgressReceiver(DownloadWatcherManager watcherManager) {
        this.watcherManager = watcherManager;
        bundler = new DownloadableBundler();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ProgressUpdater.Action action = ProgressUpdater.Action.valueOf(intent.getAction());
        handleIntent(context, intent, action);
    }

    private void handleIntent(Context context, Intent intent, ProgressUpdater.Action action) {
        switch (action) {
            case UPDATE:
                handleUpdate(intent);
                break;
            case START:
                handleStart(intent);
                break;
            case STOP:
                handleStop(context);
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

    private void handleStop(Context context) {
        watcherManager.onStop();
        unregister(context);
    }

    private void unregister(Context context) {
        context.getApplicationContext().unregisterReceiver(this);
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
