package com.ouchadam.bookkeeper;

import android.content.Context;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.foo.BookKeeperDelegate;
import com.ouchadam.bookkeeper.foo.IdManager;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;

import java.util.Arrays;

public class BasicBookKeeper implements BookKeeper {

    private final BookKeeperDelegate bookKeeperDelegate;

    public static BasicBookKeeper newInstance(Context context) {
        BookKeeperDelegate bookKeeperDelegate = BookKeeperDelegate.newInstance(context);
        return new BasicBookKeeper(bookKeeperDelegate);
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
