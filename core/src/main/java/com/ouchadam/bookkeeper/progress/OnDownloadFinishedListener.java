package com.ouchadam.bookkeeper.progress;

import com.ouchadam.bookkeeper.domain.DownloadId;

public interface OnDownloadFinishedListener {
    void onFinish(DownloadId downloadId);
}
