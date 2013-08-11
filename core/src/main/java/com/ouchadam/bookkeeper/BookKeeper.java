package com.ouchadam.bookkeeper;

import com.ouchadam.bookkeeper.watcher.DownloadWatcher;

public interface BookKeeper {
    public static final String EXTRA_DOWNLOAD_ID = "download_id";

    long keep(Downloadable downloadable);
    void watch(long downloadId, DownloadWatcher... downloadWatchers);

}
