package com.ouchadam.bookkeeper;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import com.ouchadam.bookkeeper.progress.OnAllDownloadsFinished;
import com.ouchadam.bookkeeper.progress.OnDownloadFinishedListener;
import com.ouchadam.bookkeeper.progress.ProgressReceiver;
import com.ouchadam.bookkeeper.service.WatchService;
import com.ouchadam.bookkeeper.util.ServiceUtil;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.DownloadWatcherManager;

import java.util.Arrays;
import java.util.List;

public class BasicBookKeeper implements BookKeeper, OnDownloadFinishedListener, OnAllDownloadsFinished {

    private final Context context;
    private final DownloadManager downloadManager;
    private final DownloadWatcherManager downloadWatcherManager;
    private final IdManager idManager;

    private ProgressReceiver progressReceiver;

    public static BasicBookKeeper newInstance(Context context) {
        return new BasicBookKeeper(
                context.getApplicationContext(),
                (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE),
                new DownloadWatcherManager(),
                new IdManager(new DownloaderHelper(context), context.getSharedPreferences(BookKeeper.class.getSimpleName(), Activity.MODE_PRIVATE)));
    }

    BasicBookKeeper(Context context, DownloadManager downloadManager, DownloadWatcherManager downloadWatcherManager, IdManager idManager) {
        this.context = context;
        this.downloadManager = downloadManager;
        this.downloadWatcherManager = downloadWatcherManager;
        this.idManager = idManager;
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
        idManager.onFinish(downloadId);
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
        downloadWatcherManager.clear();
        detach();
    }

    public void restore(IdManager.BookKeeperRestorer bookKeeperRestorer) {
        idManager.restore(bookKeeperRestorer);
    }

    public void store(long downloadId, long itemId) {
        idManager.addWithItem(downloadId, itemId);
    }
}
