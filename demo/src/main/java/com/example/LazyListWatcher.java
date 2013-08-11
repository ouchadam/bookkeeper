package com.example;

import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;

class LazyListWatcher implements LazyWatcher {

    private final ListItemWatcher.ItemWatcher itemWatcher;

    LazyListWatcher(ListItemWatcher.ItemWatcher itemWatcher) {
        this.itemWatcher = itemWatcher;
    }

    @Override
    public DownloadWatcher create(long downloadId, long itemId) {
        return new ListItemWatcher(itemWatcher, itemId, downloadId);
    }
}
