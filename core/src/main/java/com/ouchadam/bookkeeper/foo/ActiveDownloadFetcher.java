package com.ouchadam.bookkeeper.foo;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import com.ouchadam.bookkeeper.domain.DownloadId;

import java.util.ArrayList;
import java.util.List;

class ActiveDownloadFetcher {

    private final Context context;

    public interface OnActiveDownloads {
        void on(List<DownloadId> activeDownloadIds);
    }

    public ActiveDownloadFetcher(Context context) {
        this.context = context;
    }

    public void getActiveDownloadIds(OnActiveDownloads onActiveDownloads) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        new InnerTask(onActiveDownloads, downloadManager).start();
    }

    private static class InnerTask extends AsyncTask<Void, Void, List<DownloadId>> {

        private final OnActiveDownloads onActiveDownloads;
        private DownloadManager downloadManager;

        private InnerTask(OnActiveDownloads onActiveDownloads, DownloadManager downloadManager) {
            this.onActiveDownloads = onActiveDownloads;
            this.downloadManager = downloadManager;
        }

        public void start() {
            execute();
        }

        @Override
        protected List<DownloadId> doInBackground(Void... voids) {
            return fetchActiveDownloadIds();
        }

        private List<DownloadId> fetchActiveDownloadIds() {
            ArrayList<DownloadId> downloadIds = new ArrayList<DownloadId>();
            Cursor cursor = downloadManager.query(createQuery());
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        long downloadId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                        downloadIds.add(new DownloadId(downloadId));
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
        protected void onPostExecute(List<DownloadId> downloadIds) {
            super.onPostExecute(downloadIds);
            onActiveDownloads.on(downloadIds);
        }
    }

}
