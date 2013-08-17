package com.ouchadam.bookkeeper.progress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.ouchadam.bookkeeper.domain.DownloadId;

public class ProgressReceiver extends BroadcastReceiver {

    private KeeperIntentHandler keeperIntentHandler;

    public ProgressReceiver(KeeperIntentHandler keeperIntentHandler) {
        this.keeperIntentHandler = keeperIntentHandler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ProgressUpdater.Action action = ProgressUpdater.Action.valueOf(intent.getAction());
        DownloadId downloadId = new DownloadIdFactory().from(intent);
        if (downloadId.isValid()) {
            keeperIntentHandler.handleIntent(downloadId, intent, action);
        }
    }

    public void register(Context context) {
        IntentFilter intentFilter = getIntentFilter();
        context.registerReceiver(this, intentFilter);
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        for (ProgressUpdater.Action action : ProgressUpdater.Action.values()) {
            intentFilter.addAction(action.name());
        }
        return intentFilter;
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

}
