package com.ouchadam.bookkeeper.service;

import android.app.DownloadManager;
import android.database.Cursor;
import com.ouchadam.bookkeeper.progress.ProgressValues;

class DownloadUpdater {

    private static final int REQUERY_PAUSE_MS = 100;

    private final DownloadManager downloadManager;
    private final FileDownloadProgressWatcher progressWatcher;

    DownloadUpdater(DownloadManager downloadManager, FileDownloadProgressWatcher progressWatcher) {
        this.downloadManager = downloadManager;
        this.progressWatcher = progressWatcher;
    }

    public void watch() {
        boolean downloading = true;
        while (downloading) {
            Cursor cursor = getDownloadCursor();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        downloading = isDownloading(cursor);
                        int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        handleStatus(downloadStatus, cursor);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                sleepThread();
            }
        }
    }

    private void handleStatus(int downloadStatus, Cursor cursor) {
        long downloadId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));

        switch (downloadStatus) {
            case DownloadManager.STATUS_RUNNING:
                updateProgress(downloadId, cursor);
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                progressWatcher.onFinish(downloadId);
                break;

            default:
                break;
        }
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
        return getDownloadStatus(cursor) != DownloadManager.STATUS_SUCCESSFUL;
    }

    private int getDownloadStatus(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
    }

    private Cursor getDownloadCursor() {
        DownloadManager.Query query = createQuery();
        return downloadManager.query(query);
    }

    private DownloadManager.Query createQuery() {
        DownloadManager.Query q = new DownloadManager.Query();
        q.setFilterByStatus(DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_FAILED);
        return q;
    }
}
