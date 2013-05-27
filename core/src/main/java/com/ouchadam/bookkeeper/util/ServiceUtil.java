package com.ouchadam.bookkeeper.util;

import android.app.ActivityManager;
import android.content.Context;

public class ServiceUtil {

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
