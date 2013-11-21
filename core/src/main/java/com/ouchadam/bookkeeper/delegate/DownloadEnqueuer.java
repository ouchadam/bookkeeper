package com.ouchadam.bookkeeper.delegate;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;

import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;

class DownloadEnqueuer {

    private final DownloadManager downloadManager;

    DownloadEnqueuer(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    public long enqueue(Downloadable downloadable) {
        return start(downloadable.url(), downloadable.fileName(), downloadable.title(), downloadable.description());
    }

    private long start(Uri url, String file, String title, String description) {
        DownloadManager.Request request = createRequest(url, file, title, description);
        return downloadManager.enqueue(request);
    }

    private DownloadManager.Request createRequest(Uri url, String file, String title, String description) {
        DownloadManager.Request request = new DownloadManager.Request(url);
        request.setDescription(description);
        request.setTitle(title);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file);
        return request;
    }

    public void remove(DownloadId... downloadIds) {
        if (downloadIds.length > 0) {
            downloadManager.remove(toValues(downloadIds));
        }
    }

    private long[] toValues(DownloadId[] downloadIds) {
        long[] ids = new long[downloadIds.length];
        for (int index = 0; index < downloadIds.length; index++) {
            ids[index] = downloadIds[index].value();
        }
        return ids;
    }
}
