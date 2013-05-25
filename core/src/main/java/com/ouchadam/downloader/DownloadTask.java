package com.ouchadam.downloader;

import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

public class DownloadTask extends AsyncTask<Downloadable, ProgressValues, Void> implements UpdateNotifier.ProgressPublisher {

    private final FileDownloader fileDownloader;
    private final List<DownloadWatcher> downloadWatchers;
    private int previousPercentage = 0;

    public DownloadTask(FileDownloader fileDownloader, DownloadWatcher... downloadWatchers) {
        this.fileDownloader = fileDownloader;
        this.downloadWatchers = Arrays.asList(downloadWatchers);
    }

    @Override
    public void publish(ProgressValues progressValues) {
        if (progressValues.getPercentage() > this.previousPercentage) {
            publishProgress(progressValues);
        }
        this.previousPercentage = progressValues.getPercentage();
    }

    public interface OnFinish {
        void onFinish();
    }

    public void start(Downloadable downloadable) {
        execute(downloadable);
    }

    @Override
    protected Void doInBackground(Downloadable... params) {
        Downloadable downloadable = params[0];
        fileDownloader.init(downloadable.url(), downloadable.fileName());
        onStart(downloadable);
        fileDownloader.downloadFile();
        return null;
    }

    @Override
    protected void onProgressUpdate(ProgressValues... values) {
        super.onProgressUpdate(values);
        ProgressValues value = values[0];
        onUpdate(value);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onStop();
    }

    private void onUpdate(ProgressValues progressValues) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onUpdate(progressValues);
        }
    }

    private void onStart(Downloadable downloadable) {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onStart(downloadable);
        }
    }

    private void onStop() {
        for (DownloadWatcher downloadWatcher : downloadWatchers) {
            downloadWatcher.onStop();
        }
    }

}
