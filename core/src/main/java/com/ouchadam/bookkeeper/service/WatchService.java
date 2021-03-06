package com.ouchadam.bookkeeper.service;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.ouchadam.bookkeeper.domain.ProgressValues;
import com.ouchadam.bookkeeper.progress.ProgressUpdater;

public class WatchService extends IntentService implements FileDownloadProgressWatcher {

    private ProgressUpdater progressUpdater;

    public WatchService() {
        super(WatchService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (validIntent(intent)) {
            progressUpdater = new ProgressUpdater(this);
            watchDownload();
            progressUpdater.broadcastAllDownloadsFinished();
        }
    }

    private boolean validIntent(Intent intent) {
        return intent != null;
    }

    private void watchDownload() {
        DownloadUpdater downloadUpdater = new DownloadUpdater((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE), this);
        downloadUpdater.watch();
    }

    @Override
    public void onUpdate(long downloadId, ProgressValues progressValues) {
        progressUpdater.broadcastUpdate(downloadId, progressValues);
    }

    @Override
    public void onFinish(long downloadId) {
        progressUpdater.broadcastFinish(downloadId);
    }

}
