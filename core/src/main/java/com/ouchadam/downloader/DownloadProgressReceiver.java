package com.ouchadam.downloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.ouchadam.downloader.bundle.Bundler;
import com.ouchadam.downloader.bundle.DownloadableBundler;

import java.util.Arrays;
import java.util.List;

public class DownloadProgressReceiver extends BroadcastReceiver {

    private final List<DownloadWatcher> downloadWatchers;
    private final Bundler<Downloadable> bundler;

    public DownloadProgressReceiver(DownloadWatcher... downloadWatchers) {
        this.downloadWatchers = Arrays.asList(downloadWatchers);
        bundler = new DownloadableBundler();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ProgressUpdater.ACTION.UPDATE.name())) {
            ProgressValues values = (ProgressValues) intent.getSerializableExtra("values");
            onUpdate(values);
            return;
        }

        if (intent.getAction().equals(ProgressUpdater.ACTION.START.name())) {
            Bundle downloadable = intent.getBundleExtra("downloadable");
            onStart(bundler.from(downloadable));
            return;
        }

        if (intent.getAction().equals(ProgressUpdater.ACTION.STOP.name())) {
            onStop(context);
            return;
        }

    }

    private void onUpdate(ProgressValues progressValues) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onUpdate(progressValues);
        }
    }

    private void onStart(Downloadable downloadable) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onStart(downloadable);
        }
    }

    private void onStop(Context context) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onStop();
        }
        context.getApplicationContext().unregisterReceiver(this);
    }

    public void register(Context context) {
        IntentFilter intentFilter = getIntentFilter();
        context.getApplicationContext().registerReceiver(this, intentFilter);
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        for (ProgressUpdater.ACTION action : ProgressUpdater.ACTION.values()) {
            intentFilter.addAction(action.name());
        }
        return intentFilter;
    }

}
