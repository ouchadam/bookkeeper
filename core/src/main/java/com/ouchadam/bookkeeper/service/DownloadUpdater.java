package com.ouchadam.bookkeeper.service;

import android.app.DownloadManager;
import android.database.Cursor;
import com.ouchadam.bookkeeper.domain.ProgressValues;

import java.util.ArrayList;
import java.util.List;

class DownloadUpdater {

    private static final int REQUERY_PAUSE_MS = 100;

    private final DownloadManager downloadManager;
    private final FileDownloadProgressWatcher progressWatcher;

    List<Long> downloadIds;

    DownloadUpdater(DownloadManager downloadManager, FileDownloadProgressWatcher progressWatcher) {
        this.downloadManager = downloadManager;
        this.progressWatcher = progressWatcher;
    }

    public void watch() {
        downloadIds = new ArrayList<Long>();
        boolean allDownloadsFinished = false;
        while (!allDownloadsFinished) {
            Cursor cursor = getDownloadCursor();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        long downloadId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) != DownloadManager.STATUS_SUCCESSFUL) {
                            downloadIds.add(downloadId);
                        }
                        handleCursor(downloadId, cursor);
                        allDownloadsFinished = downloadIds.isEmpty();
                    } while (cursor.moveToNext());
                }
                cursor.close();
                sleepThread();
            }
        }
    }

    private void handleCursor(long downloadId, Cursor cursor) {
        boolean isDownloading = isDownloading(cursor);
        if (isDownloading) {
            updateProgress(downloadId, cursor);
        } else if (hasntCalledFinish(downloadId) && isDownloaded(cursor)) {
            progressWatcher.onFinish(downloadId);
            downloadIds.remove(downloadId);
        }
    }

    private boolean hasntCalledFinish(long downloadId) {
        return downloadIds.contains(downloadId);
    }

    private boolean isDownloaded(Cursor cursor) {
        return getDownloadedBytes(cursor) == getTotalBytes(cursor);
    }

    private int getDownloadedBytes(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
    }

    private int getTotalBytes(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
    }

    private void updateProgress(long downloadId, Cursor cursor) {
        int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
        progressWatcher.onUpdate(downloadId, new ProgressValues(bytes_downloaded, bytes_total));
    }

    private void sleepThread() {
        try {
            Thread.sleep(REQUERY_PAUSE_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isDownloading(Cursor cursor) {
        return !isDownloaded(cursor);
    }

    private Cursor getDownloadCursor() {
        DownloadManager.Query query = createQuery();
        return downloadManager.query(query);
    }

    private DownloadManager.Query createQuery() {
        DownloadManager.Query q = new DownloadManager.Query();
        q.setFilterByStatus(DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING | DownloadManager.STATUS_FAILED | DownloadManager.STATUS_SUCCESSFUL);
        return q;
    }
}
