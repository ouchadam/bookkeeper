package com.ouchadam.bookkeeper.watcher;

import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.ProgressValues;
import com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress;

import static com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress.Stage.*;

public class ListItemWatcher implements DownloadWatcher {

    private final ItemWatcher itemWatcher;
    private final long itemId;
    private final DownloadId downloadId;

    public interface ItemWatcher {
        void setStageFor(long itemId, ListItemProgress.Stage stage);
        void updateProgressValuesFor(long itemId, ProgressValues progressValues);
        void notifyAdapter();
    }

    public ListItemWatcher(ItemWatcher itemWatcher, long itemId, DownloadId downloadId) {
        this.itemWatcher = itemWatcher;
        this.itemId = itemId;
        this.downloadId = downloadId;
        this.itemWatcher.setStageFor(itemId, IDLE);
    }

    @Override
    public boolean isWatching(DownloadId downloadId) {
        return this.downloadId.equals(downloadId);
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
