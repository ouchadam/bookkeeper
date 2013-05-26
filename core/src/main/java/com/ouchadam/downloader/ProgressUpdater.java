package com.ouchadam.downloader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ProgressUpdater {

    static final String DOWNLOADABLE = "downloadable";
    static final String PROGRESS_VALUES = "values";

    private final Context context;

    enum Action {
        START,
        UPDATE,
        STOP;
    }

    public ProgressUpdater(Context context) {
        this.context = context.getApplicationContext();
    }

    public void broadcastStart(Bundle bundledDownloadable) {
        Intent intent = new Intent(Action.START.name());
        intent.putExtra(DOWNLOADABLE, bundledDownloadable);
        sendBroadcast(intent);
    }

    public void broadcastUpdate(ProgressValues progressValues) {
        Intent intent = new Intent(Action.UPDATE.name());
        intent.putExtra(PROGRESS_VALUES, progressValues);
        sendBroadcast(intent);
    }

    public void broadcastFinish() {
        Intent intent = new Intent(Action.STOP.name());
        sendBroadcast(intent);
    }

    private void sendBroadcast(Intent intent) {
        context.sendBroadcast(intent);
    }

}
