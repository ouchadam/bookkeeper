package com.ouchadam.bookkeeper.delegate;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.ouchadam.bookkeeper.BookKeeper;
import com.ouchadam.bookkeeper.watcher.DownloadWatcherManager;

class BookKeeperFactory {

    public static BookKeeperDelegate create(Context context) {
        Context applicationContext = context.getApplicationContext();
        DownloadEnqueuer downloadEnqueuer = new DownloadEnqueuer((DownloadManager) applicationContext.getSystemService(Context.DOWNLOAD_SERVICE), new FileNameEnforcer());
        SharedPreferences keeperPreferences = applicationContext.getSharedPreferences(BookKeeper.class.getSimpleName(), Activity.MODE_PRIVATE);
        IdManager idManager = new IdManager(ActiveDownloadFetcher.from(applicationContext), keeperPreferences);
        WatcherServiceStarter watcherService = new WatcherServiceStarter(applicationContext);
        return new BookKeeperDelegate(applicationContext, downloadEnqueuer, new DownloadWatcherManager(), idManager, watcherService);
    }

}
