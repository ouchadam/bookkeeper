package com.ouchadam.bookkeeper.progress;

import android.content.Context;
import android.content.Intent;
import com.ouchadam.bookkeeper.domain.ProgressValues;

public class ProgressUpdater {

    static final String PROGRESS_VALUES = "values";
    static final String EXTRA_DOWNLOAD_ID = "download_id";

    private final Context context;

    enum Action {
        UPDATE,
        STOP,
        ALL_DOWNLOADS_FINISHED;
    }

    public ProgressUpdater(Context context) {
        this.context = context.getApplicationContext();
    }

    public void broadcastUpdate(long downloadId, ProgressValues progressValues) {
        Intent intent = new Intent(Action.UPDATE.name());
        intent.putExtra(EXTRA_DOWNLOAD_ID, downloadId);
        intent.putExtra(PROGRESS_VALUES, progressValues);
        sendBroadcast(intent);
    }

    public void broadcastFinish(long downloadId) {
        Intent intent = new Intent(Action.STOP.name());
        intent.putExtra(EXTRA_DOWNLOAD_ID, downloadId);
        sendBroadcast(intent);
    }

    public void broadcastAllDownloadsFinished() {
        Intent intent = new Intent(Action.ALL_DOWNLOADS_FINISHED.name());
        sendBroadcast(intent);
    }

    private void sendBroadcast(Intent intent) {
        context.sendBroadcast(intent);
    }

}
