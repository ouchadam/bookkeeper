package com.ouchadam.bookkeeper.delegate;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import com.ouchadam.bookkeeper.service.WatchService;

public class WatcherServiceStarter {

    private final Context context;

    public WatcherServiceStarter(Context context) {
        this.context = context;
    }

    public void startWatching() {
        if (isNotWatching()) {
            startWatcherService();
        }
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

    private static class ServiceUtil {

        public static boolean isRunning(Context context, Class service) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo currentRunningService : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (service.getName().equals(currentRunningService.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }

    }

}
