package com.ouchadam.bookkeeper;

import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;

public interface BookKeeper {
    DownloadId keep(Downloadable downloadable);
    void watch(DownloadId downloadId, DownloadWatcher... downloadWatchers);
    void delete(DownloadId... downloadIds);
}
