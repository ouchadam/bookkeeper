package com.ouchadam.bookkeeper;

import android.content.Context;
import android.content.Intent;

import com.ouchadam.bookkeeper.bundle.Bundler;
import com.ouchadam.bookkeeper.bundle.DownloadableBundler;
import com.ouchadam.bookkeeper.queue.KeeperQueue;
import com.ouchadam.bookkeeper.util.ServiceUtil;

import java.util.Arrays;
import java.util.List;

import static com.ouchadam.bookkeeper.DownloadProgressReceiver.OnDownloadFinishedListener;

public class BookKeeper {

    static final String BUNDLE = "bundle";

    private DownloadProgressReceiver downloadProgressReceiver;
    private Context context;
    private DownloadWatcherManager downloadWatcherManager;
    private final KeeperQueue keeperQueue;

    public BookKeeper(Context context) {
        this.context = context;
        downloadWatcherManager = new DownloadWatcherManager();
        keeperQueue = new KeeperQueue();
    }

    public boolean serviceIsRunning() {
        return ServiceUtil.isRunning(context, DownloadService.class);
    }

    public void keep(Downloadable downloadable, DownloadWatcher... downloadWatchers) {
        keep(downloadable, Arrays.asList(downloadWatchers));
    }

    public void keep(Downloadable downloadable, List<DownloadWatcher> downloadWatchers) {
        keeperQueue.push(downloadable, downloadWatchers);
        if (!serviceIsRunning()) {
            keep(keeperQueue.pop());
        }
    }

    private void keep(KeeperQueue.QueuedKeep queuedKeep) {
        startDownload(queuedKeep.getDownloadable(), queuedKeep.getDownloadWatchers());
    }

    private void startDownload(Downloadable downloadable, List<DownloadWatcher> downloadWatchers) {
        attachWatchers(downloadable, downloadWatchers);
        startDownloadService(downloadable);
    }

    private void initProgressReciever(DownloadWatcherManager downloadWatcherManager) {
        downloadProgressReceiver = new DownloadProgressReceiver(downloadWatcherManager, onDownloadFinishedListener);
        downloadProgressReceiver.register(context);
    }

    private final OnDownloadFinishedListener onDownloadFinishedListener = new OnDownloadFinishedListener() {
        @Override
        public void onFinish() {
            if (keeperQueue.hasNext()) {
                keep(keeperQueue.pop());
            } else {
                detach();
            }
        }
    };

    private void startDownloadService(Downloadable downloadable) {
        Intent service = createServiceIntent(downloadable);
        context.startService(service);
    }

    private Intent createServiceIntent(Downloadable downloadable) {
        Intent service = new Intent(context, DownloadService.class);
        Bundler<Downloadable> bundler = new DownloadableBundler();
        service.putExtra(BUNDLE, bundler.to(downloadable));
        return service;
    }

    public void attachWatchers(Downloadable downloadable, DownloadWatcher... downloadWatchers) {
        attachWatchers(downloadable, Arrays.asList(downloadWatchers));
    }

    public void attachWatchers(Downloadable downloadable, List<DownloadWatcher> downloadWatchers) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            attachWatcher(downloadable, downloadWatcher);
        }
        initProgressReciever(downloadWatcherManager);
    }

    public void attachWatcher(Downloadable downloadable, DownloadWatcher downloadWatcher) {
        if (serviceIsRunning()) {
            downloadWatcher.onStart(downloadable);
        }
        downloadWatcherManager.addWatcher(downloadWatcher);
    }

    public void detach() {
        if (downloadProgressReceiver != null) {
            try {
                downloadProgressReceiver.unregister(context);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            downloadProgressReceiver = null;
        }
    }

}
