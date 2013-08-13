package com.ouchadam.bookkeeper.watcher;

import android.view.View;
import android.widget.ProgressBar;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.ProgressValues;

public class ProgressBarWatcher implements DownloadWatcher {

    private final ProgressBar progressBar;
    private final DownloadId downloadId;

    public ProgressBarWatcher(ProgressBar progressBar, DownloadId downloadId) {
        this.downloadId = downloadId;
        validate(progressBar);
        this.progressBar = progressBar;
    }

    private void validate(ProgressBar progressBar) {
        if (progressBar == null) {
            throw new NullPointerException("Progress bar is null");
        }
    }

    @Override
    public boolean isWatching(DownloadId downloadId) {
        return this.downloadId.equals(downloadId);
    }

    @Override
    public void onStart() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        progressBar.setProgress(0);
    }

    @Override
    public void onUpdate(ProgressValues progressValues) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(progressValues.getPercentage());
    }

    @Override
    public void onStop() {
        progressBar.setVisibility(View.GONE);
    }

}