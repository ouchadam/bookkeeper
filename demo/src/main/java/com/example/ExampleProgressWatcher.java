package com.example;

import android.view.View;
import android.widget.ProgressBar;
import com.ouchadam.downloader.DownloadWatcher;
import com.ouchadam.downloader.Downloadable;
import com.ouchadam.downloader.ProgressValues;

public class ExampleProgressWatcher implements DownloadWatcher {

    private final ProgressBar progressBar;

    public ExampleProgressWatcher(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void onStart(Downloadable downloadable) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        progressBar.setProgress(0);
    }

    @Override
    public void onUpdate(ProgressValues progressValues) {
        progressBar.setProgress(progressValues.getPercentage());
    }

    @Override
    public void onStop() {
        progressBar.setVisibility(View.GONE);
    }

}