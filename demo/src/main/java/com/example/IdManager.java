package com.example;

import android.os.Bundle;
import com.ouchadam.bookkeeper.progress.OnDownloadFinishedListener;

class IdManager implements OnDownloadFinishedListener {

    private static final String EXTRA_BUNDLE = "test";
    private final Bundle bundle;

    public interface BookKeeperRestorer {
        void onRestore(long downloadId, int position);
    }

    IdManager(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(EXTRA_BUNDLE)) {
            this.bundle = new Bundle();
        } else {
            this.bundle = savedInstanceState.getBundle("test");
        }
    }

    public void add(long downloadId) {
        bundle.putInt(idToKey(downloadId), -1);
    }

    public void addWithPosition(long downloadId, int position) {
        bundle.putInt(idToKey(downloadId), position);
    }


    @Override
    public void onFinish(long downloadId) {
        bundle.remove(idToKey(downloadId));
    }

    private String idToKey(long downloadId) {
        return String.valueOf(downloadId);
    }

    public void restore(BookKeeperRestorer bookKeeperRestorer) {
        if (!bundle.isEmpty()) {
            for (String key : bundle.keySet()) {
                int downloadPosition = bundle.getInt(key);
                long downloadId = Long.parseLong(key);
                bookKeeperRestorer.onRestore(downloadId, downloadPosition);
            }
        }
    }

    public void save(Bundle outState) {
        outState.putBundle("test", bundle);
    }

}
