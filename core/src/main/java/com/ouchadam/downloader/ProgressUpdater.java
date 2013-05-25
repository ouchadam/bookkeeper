package com.ouchadam.downloader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ProgressUpdater {

    private final Context context;

    enum ACTION {
        START,
        UPDATE,
        STOP;
    }

    public ProgressUpdater(Context context) {
        this.context = context.getApplicationContext();
    }

    public void broadcastStart(Bundle bundledDownloadable) {
        Intent intent = new Intent(ACTION.START.name());
        intent.putExtra("downloadable", bundledDownloadable);
        sendBroadcast(intent);
    }

    public void broadcastUpdate(ProgressValues progressValues) {
        Intent intent = new Intent(ACTION.UPDATE.name());
        intent.putExtra("values", progressValues);
        sendBroadcast(intent);
    }

    public void broadcastFinish() {
        Intent intent = new Intent(ACTION.STOP.name());
        sendBroadcast(intent);
    }

    private void sendBroadcast(Intent intent) {
        context.sendBroadcast(intent);
    }
}
