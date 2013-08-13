package com.ouchadam.bookkeeper;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.DownloadWatcherManager;

import java.util.Arrays;

public class BasicBookKeeper implements BookKeeper {

    private final BookKeeperDelegate bookKeeperDelegate;

    public static BasicBookKeeper newInstance(Context context) {
        BookKeeperDelegate bookKeeperDelegate = createFooManager(context);
        return new BasicBookKeeper(bookKeeperDelegate);
    }

    private static BookKeeperDelegate createFooManager(Context context) {
        DownloadEnqueuer downloadEnqueuer = new DownloadEnqueuer((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE));
        SharedPreferences keeperPreferences = context.getSharedPreferences(BookKeeper.class.getSimpleName(), Activity.MODE_PRIVATE);
        IdManager idManager = new IdManager(new ActiveDownloadFetcher(context), keeperPreferences);
        WatcherServiceStarter watcherService = new WatcherServiceStarter(context);
        return new BookKeeperDelegate(context, downloadEnqueuer, new DownloadWatcherManager(), idManager, watcherService);
    }

    BasicBookKeeper(BookKeeperDelegate bookKeeperDelegate) {
        this.bookKeeperDelegate = bookKeeperDelegate;
    }

    @Override
    public DownloadId keep(Downloadable downloadable) {
        return bookKeeperDelegate.start(downloadable);
    }

    @Override
    public void watch(DownloadId downloadId, DownloadWatcher... downloadWatchers) {
        bookKeeperDelegate.startListeningForUpdates(downloadId, Arrays.asList(downloadWatchers));
    }

    public void restore(IdManager.BookKeeperRestorer bookKeeperRestorer) {
        bookKeeperDelegate.restore(bookKeeperRestorer);
    }

    public void store(DownloadId downloadId, long itemId) {
        bookKeeperDelegate.store(downloadId, itemId);
    }
}
