package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.ouchadam.bookkeeper.BasicBookKeeper;
import com.ouchadam.bookkeeper.Downloadable;
import com.ouchadam.bookkeeper.IdManager;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;

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
    public void watch(long downloadId, DownloadWatcher... downloadWatcher) {
        bookKeeper.watch(downloadId, downloadWatcher);
    }

    @Override
    public void store(long downloadId, long itemId) {
        bookKeeper.store(downloadId, itemId);
    }

    @Override
    public long keep(Downloadable downloadable) {
        return bookKeeper.keep(downloadable);
    }

    @Override
    public void restore(final LazyWatcher lazyWatcher) {
        bookKeeper.restore(new IdManager.BookKeeperRestorer() {
            @Override
            public void onRestore(long downloadId, long itemId) {
                bookKeeper.watch(downloadId, lazyWatcher.create(downloadId, itemId));
            }
        });
    }
}

