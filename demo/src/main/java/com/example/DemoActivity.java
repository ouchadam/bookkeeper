package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.ouchadam.bookkeeper.BookKeeper;
import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.Downloadable;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.bookkeeper.watcher.ProgressBarWatcher;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends Activity implements View.OnClickListener {

    private ExampleListAdapter adapter;
    private ListView listView;

    private BookKeeper bookKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.download_start).setOnClickListener(this);
        initList();
    }

    private void initList() {
        listView = (ListView) findViewById(R.id.list_view);
        List<String> data = createAdapterData();
        adapter = new ExampleListAdapter(R.layout.list_item, R.id.list_item_progress_bar, LayoutInflater.from(this), data);
        listView.setAdapter(adapter);
    }

    private List<String> createAdapterData() {
        List<String> data = new ArrayList<String>();
        data.add("item one");
        data.add("item 2");
        return data;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBookKeeper();
    }

    private void initBookKeeper() {
        bookKeeper = new BookKeeper(this);
        if (bookKeeper.serviceIsRunning()) {
            bookKeeper.attachWatchers(getDownloadable(), getWatchers());
        }
    }

    @Override
    public void onClick(View v) {
        bookKeeper.keep(getDownloadable(), getWatchers());
    }

    private ProgressBar getProgressBar() {
        return (ProgressBar) findViewById(R.id.main_progress_bar);
    }

    private Downloadable getDownloadable() {
        Downloadable downloadable = new ExampleDownloadable();
        return downloadable;
    }

    private DownloadWatcher[] getWatchers() {
        DownloadWatcher[] downloadWatchers = new DownloadWatcher[3];
        downloadWatchers[0] = new NotificationWatcher(this);
        downloadWatchers[1] = new ListItemWatcher(adapter, adapter.getItemId(0));
        downloadWatchers[2] = new ProgressBarWatcher(getProgressBar());
        return downloadWatchers;
    }

}

