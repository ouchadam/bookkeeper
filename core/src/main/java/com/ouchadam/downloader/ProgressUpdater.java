package com.ouchadam.downloader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ProgressUpdater {

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
        intent.putExtra("downloadable", bundledDownloadable);
        sendBroadcast(intent);
    }

    public void broadcastUpdate(ProgressValues progressValues) {
        Intent intent = new Intent(Action.UPDATE.name());
        intent.putExtra("values", progressValues);
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
