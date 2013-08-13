package com.ouchadam.bookkeeper.progress;

import com.ouchadam.bookkeeper.DownloadId;

public interface OnDownloadFinishedListener {
    void onFinish(DownloadId downloadId);
}
