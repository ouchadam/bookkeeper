package com.example;

import android.app.Activity;
import android.os.Bundle;

import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.BookKeeperRestorer;
import com.ouchadam.bookkeeper.delegate.RestoreableBookKeeper;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;

public class DemoActivity extends Activity implements Downloader {

    private RestoreableBookKeeper bookKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bookKeeper = RestoreableBookKeeper.newInstance(this);
    }

    @Override
    public void watch(DownloadId downloadId, DownloadWatcher... downloadWatcher) {
        bookKeeper.watch(downloadId, downloadWatcher);
    }

    @Override
    public void store(DownloadId downloadId, long itemId) {
        bookKeeper.store(downloadId, itemId);
    }

    @Override
    public DownloadId keep(Downloadable downloadable) {
        return bookKeeper.keep(downloadable);
    }

    @Override
    public void restore(final LazyWatcher lazyWatcher) {
        bookKeeper.restore(new BookKeeperRestorer() {
            @Override
            public void onRestore(DownloadId downloadId, long itemId) {
                bookKeeper.watch(downloadId, lazyWatcher.create(downloadId, itemId));
            }
        });
    }
}

