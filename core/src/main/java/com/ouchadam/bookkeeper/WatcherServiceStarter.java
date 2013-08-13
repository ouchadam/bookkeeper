package com.ouchadam.bookkeeper;

import android.content.Context;
import android.content.Intent;
import com.ouchadam.bookkeeper.service.WatchService;
import com.ouchadam.bookkeeper.util.ServiceUtil;

class WatcherServiceStarter {

    private final Context context;

    WatcherServiceStarter(Context context) {
        this.context = context;
    }

    public void startWatching() {
        if (isNotWatching()) {
            startWatching();
        }
        startWatcherService();
    }

    private boolean isNotWatching() {
        return !ServiceUtil.isRunning(context, WatchService.class);
    }

    private void startWatcherService() {
        Intent service = createServiceIntent();
        context.startService(service);
    }

    private Intent createServiceIntent() {
        return new Intent(context, WatchService.class);
    }

}
