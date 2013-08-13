package com.ouchadam.bookkeeper;

import com.ouchadam.bookkeeper.watcher.DownloadWatcher;

public interface BookKeeper {
    DownloadId keep(Downloadable downloadable);
    void watch(DownloadId downloadId, DownloadWatcher... downloadWatchers);

}
