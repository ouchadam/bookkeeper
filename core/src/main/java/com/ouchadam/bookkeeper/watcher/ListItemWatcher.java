package com.ouchadam.bookkeeper.watcher;

import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.Downloadable;
import com.ouchadam.bookkeeper.progress.ProgressValues;
import com.ouchadam.bookkeeper.watcher.adapter.DownloadableListAdapter;

import static com.ouchadam.bookkeeper.watcher.adapter.DownloadableListAdapter.Stage.*;

public class ListItemWatcher implements DownloadWatcher {

    private final DownloadableListAdapter listAdapter;
    private final long itemId;

    public ListItemWatcher(DownloadableListAdapter adapter, long itemId) {
        this.listAdapter = adapter;
        this.itemId = itemId;
        listAdapter.setStageFor(itemId, IDLE);
    }

    @Override
    public void onStart(Downloadable downloadable) {
        listAdapter.setStageFor(itemId, START);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdate(ProgressValues progressValues) {
        listAdapter.setStageFor(itemId, UPDATING);
        listAdapter.updateProgressValuesFor(itemId, progressValues);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        listAdapter.setStageFor(itemId, STOP);
        listAdapter.notifyDataSetChanged();
    }

}
