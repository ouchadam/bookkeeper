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

public class DemoActivity extends Activity implements AdapterView.OnItemClickListener {

    private ExampleListAdapter adapter;
    private BasicBookKeeper bookKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initList();
        bookKeeper = BasicBookKeeper.newInstance(this);
        bookKeeper.restore(restorer);
    }

    private void initList() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new ExampleListAdapter(LayoutInflater.from(this));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private final IdManager.BookKeeperRestorer restorer = new IdManager.BookKeeperRestorer() {
        @Override
        public void onRestore(long downloadId, long itemId) {
            bookKeeper.watch(downloadId, createListItemWatcher(itemId, downloadId));
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
        SimpleItem item = adapter.getItem(position);
        Downloadable downloadable = new ExampleDownloadable(item);
        long downloadId = bookKeeper.keep(downloadable);
        bookKeeper.store(downloadId, itemId);
        bookKeeper.watch(downloadId, getDownloadWatchers(itemId, downloadable, downloadId));
    }

    private DownloadWatcher[] getDownloadWatchers(long itemId, Downloadable downloadable, long downloadId) {
        return new DownloadWatcher[]{createNotificationWatcher(downloadable, downloadId), createListItemWatcher(itemId, downloadId)};
    }

    private NotificationWatcher createNotificationWatcher(Downloadable downloadable, long downloadId) {
        return new NotificationWatcher(this, downloadable, downloadId);
    }

    private ListItemWatcher createListItemWatcher(long itemId, long downloadId) {
        return new ListItemWatcher(adapter, itemId, downloadId);
    }

}

