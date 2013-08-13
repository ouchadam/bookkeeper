package com.ouchadam.bookkeeper.service;

import com.ouchadam.bookkeeper.domain.ProgressValues;

interface FileDownloadProgressWatcher {
    void onUpdate(long downloadId, ProgressValues progressValues);
    void onFinish(long downloadId);
}
