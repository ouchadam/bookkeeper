package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.ouchadam.bookkeeper.BasicBookKeeper;
import com.ouchadam.bookkeeper.BookKeeper;
import com.ouchadam.bookkeeper.Downloadable;
import com.ouchadam.bookkeeper.progress.OnDownloadFinishedListener;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;

import java.util.*;

public class DemoActivity extends Activity implements AdapterView.OnItemClickListener {

    private ExampleListAdapter adapter;
    private BookKeeper bookKeeper;
    private IdManager idManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initViews();
        idManager = new IdManager(savedInstanceState);
        bookKeeper = BasicBookKeeper.newInstance(this, idManager);
        idManager.restore(restorer);
    }

    private ListItemWatcher getListItemWatcher(long itemId, long downloadId) {
        return new ListItemWatcher(adapter, itemId, downloadId);
    }

    private void initViews() {
        initList();
    }

    private void initList() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new ExampleListAdapter(LayoutInflater.from(this), createAdapterData());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private List<SimpleItem> createAdapterData() {
        List<SimpleItem> data = new ArrayList<SimpleItem>();
        data.add(new SimpleItem("item one", "http://ipv4.download.thinkbroadband.com/5MB.zip"));
        data.add(new SimpleItem("item 2", "http://ipv4.download.thinkbroadband.com/5MB.zip"));
        return data;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
        SimpleItem item = adapter.getItem(position);
        Downloadable downloadable = new ExampleDownloadable(item);
        long downloadId = bookKeeper.keep(downloadable);
        idManager.addWithPosition(downloadId, position);
        bookKeeper.watch(downloadId, new NotificationWatcher(this, downloadable, downloadId), getListItemWatcher(itemId, downloadId));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        idManager.save(outState);
        super.onSaveInstanceState(outState);
    }

    private final IdManager.BookKeeperRestorer restorer = new IdManager.BookKeeperRestorer() {
        @Override
        public void onRestore(long downloadId, int position) {
            bookKeeper.watch(downloadId, getListItemWatcher(adapter.getItemId(position), downloadId));
        }
    };

}

