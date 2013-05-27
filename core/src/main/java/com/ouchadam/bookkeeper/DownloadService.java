package com.ouchadam.bookkeeper;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.ouchadam.bookkeeper.bundle.Bundler;
import com.ouchadam.bookkeeper.bundle.DownloadableBundler;
import com.ouchadam.bookkeeper.progress.ProgressUpdater;
import com.ouchadam.bookkeeper.progress.ProgressValues;

public class DownloadService extends IntentService implements FileDownloader.FileDownloadProgressWatcher {

    private static final int MAX_RETRIES = 1;
    private final Bundler<Downloadable> bundler;

    private int previousPercentage = 0;
    private ProgressUpdater progressUpdater;
    private int retries = 0;

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
        try {
            fileDownloader.download(downloadable.url(), downloadable.file());
        } catch (FileDownloader.FileDownloadException e) {
            e.printStackTrace();
            retry(downloadable);
        }
    }

    private void retry(Downloadable downloadable) {
        if (canRetry()) {
            retries ++;
            downloadFile(downloadable);
        }
    }

    private boolean canRetry() {
        return retries < MAX_RETRIES;
    }

    @Override
    public void onUpdate(ProgressValues progressValues) {
        if (progressValues.getPercentage() > this.previousPercentage) {
            progressUpdater.broadcastUpdate(progressValues);
            this.previousPercentage = progressValues.getPercentage();
        }
    }

}
