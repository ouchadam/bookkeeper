package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.ouchadam.bookkeeper.BookKeeper;
import com.ouchadam.bookkeeper.Downloadable;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends Activity implements View.OnClickListener {

    private ExampleListAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.download_start).setOnClickListener(this);

        listView = (ListView) findViewById(R.id.list_view);

        List<String> data = new ArrayList<String>();
        data.add("item one");
        data.add("item 2");

        adapter = new ExampleListAdapter(R.layout.list_item, R.id.progress_bar, LayoutInflater.from(this), data);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Downloadable downloadable = new ExampleDownloadable();
        NotificationWatcher notificationWatcher = new NotificationWatcher(this);
        ExampleProgressWatcher progressWatcher = new ExampleProgressWatcher(getProgressBarFromList());

        BookKeeper.keep(this, downloadable, notificationWatcher, progressWatcher);
    }

    private ProgressBar getProgressBarFromList() {
        return adapter.getProgressBarFrom(0);
    }

}

