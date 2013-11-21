package com.ouchadam.bookkeeper;

import android.content.Context;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.delegate.BookKeeperDelegate;
import com.ouchadam.bookkeeper.delegate.IdManager;

import java.util.Arrays;

public class RestoreableBookKeeper implements BookKeeper {

    private final BookKeeperDelegate bookKeeperDelegate;

    public static RestoreableBookKeeper newInstance(Context context) {
        BookKeeperDelegate bookKeeperDelegate = BookKeeperDelegate.getInstance(context);
        return new RestoreableBookKeeper(bookKeeperDelegate);
    }

    RestoreableBookKeeper(BookKeeperDelegate bookKeeperDelegate) {
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

    @Override
    public void delete(DownloadId... downloadIds) {
        bookKeeperDelegate.delete(downloadIds);
    }

    public void restore(IdManager.BookKeeperRestorer bookKeeperRestorer) {
        bookKeeperDelegate.restore(bookKeeperRestorer);
    }

    public void store(DownloadId downloadId, long itemId) {
        bookKeeperDelegate.store(downloadId, itemId);
    }
}
