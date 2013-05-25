package com.ouchadam.downloader;

public class Downloader {

    public static void download(Downloadable downloadable, DownloadWatcher... downloadWatchers) {
        UpdateNotifier updateNotifier = new UpdateNotifier();
        FileDownloader fileDownloader = new FileDownloader(updateNotifier);
        DownloadTask downloadTask = new DownloadTask(fileDownloader, downloadWatchers);
        updateNotifier.setProgressPublisher(downloadTask);
        downloadTask.start(downloadable);
    }

}
