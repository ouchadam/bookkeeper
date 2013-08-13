package com.ouchadam.bookkeeper;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.DownloadWatcherManager;

import java.util.Arrays;

public class BasicBookKeeper implements BookKeeper {

    private final FooManager fooManager;

    public static BasicBookKeeper newInstance(Context context) {
        FooManager fooManager = createFooManager(context);
        return new BasicBookKeeper(fooManager);
    }

    private static FooManager createFooManager(Context context) {
        DownloadEnqueuer downloadEnqueuer = new DownloadEnqueuer((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE));
        SharedPreferences keeperPreferences = context.getSharedPreferences(BookKeeper.class.getSimpleName(), Activity.MODE_PRIVATE);
        IdManager idManager = new IdManager(new ActiveDownloadFetcher(context), keeperPreferences);
        WatcherServiceStarter watcherService = new WatcherServiceStarter(context);
        return new FooManager(context, downloadEnqueuer, new DownloadWatcherManager(), idManager, watcherService);
    }

    BasicBookKeeper(FooManager fooManager) {
        this.fooManager = fooManager;
    }

    @Override
    public DownloadId keep(Downloadable downloadable) {
        return fooManager.start(downloadable);
    }

    @Override
    public void watch(DownloadId downloadId, DownloadWatcher... downloadWatchers) {
        attachWatchers(downloadWatchers);
        fooManager.startListeningForUpdates(downloadId);
    }

    private void attachWatchers(DownloadWatcher... downloadWatcher) {
        fooManager.attachWatchers(Arrays.asList(downloadWatcher));
    }

    public void restore(IdManager.BookKeeperRestorer bookKeeperRestorer) {
        fooManager.restore(bookKeeperRestorer);
    }

    public void store(DownloadId downloadId, long itemId) {
        fooManager.store(downloadId, itemId);
    }
}
