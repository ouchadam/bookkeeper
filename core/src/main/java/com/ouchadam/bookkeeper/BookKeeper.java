package com.ouchadam.bookkeeper;

import android.content.Context;
import android.content.Intent;

import com.ouchadam.bookkeeper.bundle.Bundler;
import com.ouchadam.bookkeeper.bundle.DownloadableBundler;
import com.ouchadam.bookkeeper.util.ServiceUtil;

import static com.ouchadam.bookkeeper.DownloadProgressReceiver.OnDownloadFinishedListener;

public class BookKeeper {

    static final String BUNDLE = "bundle";

    private DownloadProgressReceiver downloadProgressReceiver;
    private Context context;
    private DownloadWatcherManager downloadWatcherManager;

    public BookKeeper(Context context) {
        this.context = context;
        downloadWatcherManager = new DownloadWatcherManager();
    }

    public boolean serviceIsRunning() {
        return ServiceUtil.isRunning(context, DownloadService.class);
    }

    public void keep(Downloadable downloadable, DownloadWatcher... downloadWatchers) {
        if (!serviceIsRunning()) {
            attachWatchers(downloadable, downloadWatchers);
            startDownloadService(downloadable);
        }
    }

    private void initProgressReciever(DownloadWatcherManager downloadWatcherManager) {
        downloadProgressReceiver = new DownloadProgressReceiver(downloadWatcherManager, onDownloadFinishedListener);
        downloadProgressReceiver.register(context);
    }

    private final OnDownloadFinishedListener onDownloadFinishedListener = new OnDownloadFinishedListener() {
        @Override
        public void onFinish() {
            detach();
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
            } catch (IllegalArgumentException e)  {
                e.printStackTrace();
            }
            downloadProgressReceiver = null;
        }
    }

}
