package com.ouchadam.bookkeeper;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import com.ouchadam.bookkeeper.progress.OnAllDownloadsFinished;
import com.ouchadam.bookkeeper.progress.ProgressReceiver;
import com.ouchadam.bookkeeper.service.WatchService;
import com.ouchadam.bookkeeper.util.ServiceUtil;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;

import java.util.Arrays;
import java.util.List;

import com.ouchadam.bookkeeper.progress.OnDownloadFinishedListener;
import com.ouchadam.bookkeeper.watcher.DownloadWatcherManager;

public class BasicBookKeeper implements BookKeeper, OnDownloadFinishedListener, OnAllDownloadsFinished {

    private final Context context;
    private final DownloadManager downloadManager;
    private final DownloadWatcherManager downloadWatcherManager;
    private final OnDownloadFinishedListener onDownloadFinishedListener;

    private ProgressReceiver progressReceiver;

    public static BookKeeper newInstance(Context context, OnDownloadFinishedListener onDownloadFinishedListener) {
        return new BasicBookKeeper(
                context.getApplicationContext(),
                (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE),
                new DownloadWatcherManager(),
                onDownloadFinishedListener);
    }

    BasicBookKeeper(Context context, DownloadManager downloadManager, DownloadWatcherManager downloadWatcherManager, OnDownloadFinishedListener onDownloadFinishedListener) {
        this.context = context;
        this.downloadManager = downloadManager;
        this.downloadWatcherManager = downloadWatcherManager;
        this.onDownloadFinishedListener = onDownloadFinishedListener;
    }

    @Override
    public long keep(Downloadable downloadable) {
        return new DownloadEnqueuer(downloadManager).start(downloadable);
    }

    @Override
    public void watch(long downloadId, DownloadWatcher... downloadWatcher) {
        attachWatchers(Arrays.asList(downloadWatcher));
        downloadWatcherManager.onStart(downloadId);
        if (isNotWatching()) {
            startWatching(downloadId);
        }
    }

    private void attachWatchers(List<DownloadWatcher> downloadWatchers) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            attachWatcher(downloadWatcher);
        }
        initProgressReceiver();
    }

    private void attachWatcher(DownloadWatcher downloadWatcher) {
        downloadWatcherManager.addWatcher(downloadWatcher);
    }

    private void initProgressReceiver() {
        if (progressReceiver == null) {
            progressReceiver = new ProgressReceiver(downloadWatcherManager, this, this);
            progressReceiver.register(context);
        }
    }

    private boolean isNotWatching() {
        return !ServiceUtil.isRunning(context, WatchService.class);
    }

    private void startWatching(long downloadId) {
        Intent service = createServiceIntent(downloadId);
        context.startService(service);
    }

    private Intent createServiceIntent(long downloadId) {
        Intent service = new Intent(context, WatchService.class);
        service.putExtra(BookKeeper.EXTRA_DOWNLOAD_ID, downloadId);
        return service;
    }

    @Override
    public void onFinish(long downloadId) {
        onDownloadFinishedListener.onFinish(downloadId);
    }

    private void detach() {
        if (progressReceiver != null) {
            try {
                progressReceiver.unregister(context);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            progressReceiver = null;
        }
    }

    @Override
    public void onAllFinished() {
        detach();
    }
}
