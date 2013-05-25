package com.ouchadam.downloader;

public class UpdateNotifier implements FileDownloader.FileDownloadProgressWatcher {

    private ProgressPublisher progressPublisher;

    interface ProgressPublisher {
        void publish(ProgressValues progressValues);
    }

    public void setProgressPublisher(ProgressPublisher progressPublisher) {
        this.progressPublisher = progressPublisher;
    }

    @Override
    public void onUpdate(ProgressValues progressValues) {
        getProgressPublisher().publish(progressValues);
    }

    private ProgressPublisher getProgressPublisher() {
        if (progressPublisher == null) {
            throw new IllegalStateException("You must set a Progress publisher");
        }
        return progressPublisher;
    }

}
