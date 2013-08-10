package com.ouchadam.bookkeeper.watcher;

import android.view.View;
import android.widget.ProgressBar;
import com.ouchadam.bookkeeper.progress.ProgressValues;

public class ProgressBarWatcher implements DownloadWatcher {

    private final ProgressBar progressBar;
    private final long downloadId;

    public ProgressBarWatcher(ProgressBar progressBar, long downloadId) {
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
    public boolean isWatching(long downloadId) {
        return this.downloadId == downloadId;
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