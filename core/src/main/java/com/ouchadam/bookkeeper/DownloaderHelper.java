package com.ouchadam.bookkeeper;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class DownloaderHelper {

    private final Context context;

    public interface OnActiveDownloads {
        void on(List<Long> activeDownloadIds);
    }

    public DownloaderHelper(Context context) {
        this.context = context;
    }

    public void getActiveDownloadIds(OnActiveDownloads onActiveDownloads) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        new FooAsync(onActiveDownloads, downloadManager).start();
    }

    private static class FooAsync extends AsyncTask<Void, Void, List<Long>> {

        private final OnActiveDownloads onActiveDownloads;
        private DownloadManager downloadManager;

        private FooAsync(OnActiveDownloads onActiveDownloads, DownloadManager downloadManager) {
            this.onActiveDownloads = onActiveDownloads;
            this.downloadManager = downloadManager;
        }

        public void start() {
            execute();
        }

        @Override
        protected List<Long> doInBackground(Void... voids) {
            return foo();
        }

        private List<Long> foo() {
            ArrayList<Long> downloadIds = new ArrayList<Long>();
            Cursor cursor = downloadManager.query(createQuery());
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        long downloadId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                        downloadIds.add(downloadId);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            return downloadIds;
        }

        private DownloadManager.Query createQuery() {
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterByStatus(DownloadManager.STATUS_RUNNING);
            return q;
        }

        @Override
        protected void onPostExecute(List<Long> longs) {
            super.onPostExecute(longs);
            onActiveDownloads.on(longs);
        }
    }


}
