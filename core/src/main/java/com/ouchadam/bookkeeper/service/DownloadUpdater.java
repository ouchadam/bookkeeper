package com.ouchadam.bookkeeper.service;

import android.app.DownloadManager;
import android.database.Cursor;
import android.util.Log;
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
        boolean allDownloadsFinished = false;
        long startTime = System.currentTimeMillis();
        while (!allDownloadsFinished) {
            Cursor cursor = getDownloadCursor();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Log.e("!!!", "Loop : " + allDownloadsFinished + "  isDownloaded : " + isDownloaded(cursor) + " past time offset : " + outOfThres(startTime));
                        int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        handleStatus(downloadStatus, cursor);
                        allDownloadsFinished = !isDownloading(cursor) && outOfThres(startTime);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                sleepThread();
            }
        }
        Log.e("!!!", "Loop escaped");
    }

    private boolean outOfThres(long startTime) {
        return System.currentTimeMillis() > (startTime + 5000);
    }

    private void handleStatus(int downloadStatus, Cursor cursor) {
        long downloadId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));


        Log.e("!!!", "Status : " + statusToMessage(downloadStatus));
//        Log.e("!!!", "Reason : " + cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_REASON)));
//        Log.e("!!!", "Is Downloaded : " + isDownloaded(cursor)) ;
        Log.e("!!!", "DL Bytes : " + getDownloadedBytes(cursor)) ;
        Log.e("!!!", "Total : " + getTotalBytes(cursor)) ;

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

    private boolean isDownloaded(Cursor cursor) {
        return getDownloadedBytes(cursor) == getTotalBytes(cursor);
    }

    private int getDownloadedBytes(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
    }

    private int getTotalBytes(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
    }

    private String statusToMessage(int downloadStatus) {
        String msg = "???";

        switch (downloadStatus) {
            case DownloadManager.STATUS_FAILED:
                msg = "Download failed!";
                break;

            case DownloadManager.STATUS_PAUSED:
                msg = "Download paused!";
                break;

            case DownloadManager.STATUS_PENDING:
                msg = "Download pending!";
                break;

            case DownloadManager.STATUS_RUNNING:
                msg = "Download in progress!";
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Download complete!";
                break;

            default:
                msg = "Download is nowhere in sight";
                break;
        }

        return (msg);
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
        return getDownloadStatus(cursor) != DownloadManager.STATUS_SUCCESSFUL && isDownloaded(cursor);
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
