package com.ouchadam.bookkeeper.watcher;

import android.view.View;
import android.widget.ProgressBar;
import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.Downloadable;
import com.ouchadam.bookkeeper.ProgressValues;

public class ProgressBarWatcher implements DownloadWatcher {

    private final ProgressBar progressBar;

    public ProgressBarWatcher(ProgressBar progressBar) {
        validate(progressBar);
        this.progressBar = progressBar;
    }

    private void validate(ProgressBar progressBar) {
        if (progressBar == null) {
            throw new NullPointerException("Progress bar is null");
        }
    }

    @Override
    public void onStart(Downloadable downloadable) {
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