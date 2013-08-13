package com.example;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;

public class DemoFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ExampleListAdapter adapter;
    private Downloader downloader;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        downloader = (Downloader) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        initList(view);
        return view;
    }

    private void initList(View view) {
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ExampleListAdapter(LayoutInflater.from(getActivity()));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        downloader.restore(new LazyListWatcher(adapter));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
        SimpleItem item = adapter.getItem(position);
        Downloadable downloadable = new ExampleDownloadable(item);

        DownloadId downloadId = downloader.keep(downloadable);
        downloader.store(downloadId, itemId);
        downloader.watch(downloadId, getDownloadWatchers(itemId, downloadable, downloadId));
    }

    private DownloadWatcher[] getDownloadWatchers(long itemId, Downloadable downloadable, DownloadId downloadId) {
        return new DownloadWatcher[]{createNotificationWatcher(downloadable, downloadId), createListItemWatcher(itemId, downloadId)};
    }

    private NotificationWatcher createNotificationWatcher(Downloadable downloadable, DownloadId downloadId) {
        return new NotificationWatcher(getActivity(), downloadable, downloadId);
    }

    private ListItemWatcher createListItemWatcher(long itemId, DownloadId downloadId) {
        return new ListItemWatcher(adapter, itemId, downloadId);
    }

}
