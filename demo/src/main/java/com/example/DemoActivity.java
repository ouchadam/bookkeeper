package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.ouchadam.downloader.Downloadable;
import com.ouchadam.downloader.Downloader;
import com.ouchadam.downloader.watcher.NotificationWatcher;

public class DemoActivity extends Activity implements View.OnClickListener {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.download_start).setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public void onClick(View v) {
        Downloadable downloadable = new ExampleDownloadable();
        NotificationWatcher notificationWatcher = new NotificationWatcher(this);
        ExampleProgressWatcher progressWatcher = new ExampleProgressWatcher(getProgressBarFromList());

        Downloader.download(this, downloadable, notificationWatcher, progressWatcher);
    }

    private ProgressBar getProgressBarFromList() {
        return progressBar;
    }

}

