package com.ouchadam.bookkeeper.watcher;

import android.util.Log;
import com.ouchadam.bookkeeper.progress.ProgressValues;
import com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress;

import static com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress.Stage.*;

public class ListItemWatcher implements DownloadWatcher {

    private final ItemWatcher itemWatcher;
    private final long itemId;
    private long downloadId;

    public interface ItemWatcher {
        void setStageFor(long itemId, ListItemProgress.Stage stage);
        void updateProgressValuesFor(long itemId, ProgressValues progressValues);
        void notifyAdapter();
    }

    public ListItemWatcher(ItemWatcher itemWatcher, long itemId, long downloadId) {
        this.itemWatcher = itemWatcher;
        this.itemId = itemId;
        this.downloadId = downloadId;
        this.itemWatcher.setStageFor(itemId, IDLE);
    }

    @Override
    public boolean isWatching(long downloadId) {
        return this.downloadId == downloadId;
    }

    @Override
    public void onStart() {
        itemWatcher.setStageFor(itemId, START);
        itemWatcher.notifyAdapter();
    }

    @Override
    public void onUpdate(ProgressValues progressValues) {
        itemWatcher.setStageFor(itemId, UPDATING);
        itemWatcher.updateProgressValuesFor(itemId, progressValues);
        itemWatcher.notifyAdapter();
    }

    @Override
    public void onStop() {
        itemWatcher.setStageFor(itemId, STOP);
        itemWatcher.notifyAdapter();
    }

}
