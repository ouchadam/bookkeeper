package com.ouchadam.bookkeeper;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.ouchadam.bookkeeper.bundle.Bundler;
import com.ouchadam.bookkeeper.bundle.DownloadableBundler;

public class DownloadService extends IntentService implements FileDownloader.FileDownloadProgressWatcher {

    private final Bundler<Downloadable> bundler;
    private int previousPercentage = 0;
    private ProgressUpdater progressUpdater;

    public DownloadService() {
        super(DownloadService.class.getSimpleName());
        bundler = new DownloadableBundler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            progressUpdater = new ProgressUpdater(this);

            Bundle bundledDownloadable = getBundledDownloadable(intent);
            Downloadable downloadable = bundler.from(bundledDownloadable);

            progressUpdater.broadcastStart(bundledDownloadable);

            downloadFile(downloadable);

            progressUpdater.broadcastFinish();
        }
    }

    private Bundle getBundledDownloadable(Intent intent) {
        return intent.getBundleExtra(BookKeeper.BUNDLE);
    }

    private void downloadFile(Downloadable downloadable) {
        FileDownloader fileDownloader = new FileDownloader(this);
        fileDownloader.init(downloadable.url(), downloadable.file());
        fileDownloader.downloadFile();
    }

    @Override
    public void onUpdate(ProgressValues progressValues) {
        if (progressValues.getPercentage() > this.previousPercentage) {
            progressUpdater.broadcastUpdate(progressValues);
            this.previousPercentage = progressValues.getPercentage();
        }
    }

}
