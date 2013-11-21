package com.ouchadam.bookkeeper;

import com.ouchadam.bookkeeper.domain.DownloadId;

public interface BookKeeperRestorer {
    void onRestore(DownloadId downloadId, long itemId);
}
