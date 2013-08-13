package com.example;

import android.app.Activity;
import android.os.Bundle;
import com.ouchadam.bookkeeper.*;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.foo.IdManager;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;

public class DemoActivity extends Activity implements Downloader {

    private ExampleListAdapter adapter;
    private BasicBookKeeper bookKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bookKeeper = BasicBookKeeper.newInstance(this);
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
        bookKeeper.restore(new IdManager.BookKeeperRestorer() {
            @Override
            public void onRestore(DownloadId downloadId, long itemId) {
                bookKeeper.watch(downloadId, lazyWatcher.create(downloadId, itemId));
            }
        });
    }
}

