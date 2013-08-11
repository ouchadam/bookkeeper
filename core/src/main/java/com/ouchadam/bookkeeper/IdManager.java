package com.ouchadam.bookkeeper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.ouchadam.bookkeeper.progress.OnDownloadFinishedListener;

import java.util.List;
import java.util.Map;

public class IdManager implements OnDownloadFinishedListener {

    private DownloaderHelper downloaderHelper;
    private final SharedPreferences sharedPreferences;

    public interface BookKeeperRestorer {
        void onRestore(long downloadId, long itemId);
    }

    IdManager(DownloaderHelper downloaderHelper, SharedPreferences sharedPreferences) {
        this.downloaderHelper = downloaderHelper;
        this.sharedPreferences = sharedPreferences;
    }

    public void addWithItem(long downloadId, long itemId) {
        sharedPreferences.edit().putLong(idToKey(downloadId), itemId).apply();
    }

    @Override
    public void onFinish(long downloadId) {
        removeKey(idToKey(downloadId));
    }

    private void removeKey(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    private String idToKey(long downloadId) {
        return String.valueOf(downloadId);
    }

    public void restore(final BookKeeperRestorer bookKeeperRestorer) {
        if (sharedPreferences.getAll() != null && !sharedPreferences.getAll().isEmpty()) {
            downloaderHelper.getActiveDownloadIds(new DownloaderHelper.OnActiveDownloads() {
                @Override
                public void on(List<Long> activeDownloadIds) {
                    handleActiveDownloadIds(activeDownloadIds, bookKeeperRestorer);
                }
            });
        }
    }

    private void handleActiveDownloadIds(List<Long> activeDownloadIds, BookKeeperRestorer bookKeeperRestorer) {
        Map<String, Long> keys = (Map<String, Long>) sharedPreferences.getAll();
        for (Map.Entry<String, Long> entry : keys.entrySet()) {
            String key = entry.getKey();
            long itemId = getLong(entry);
            long downloadId = Long.parseLong(key);
            if (pruneIds(activeDownloadIds, key)) {
                bookKeeperRestorer.onRestore(downloadId, itemId);
            } else {
                removeKey(key);
            }
        }
    }

    private long getLong(Map.Entry<String, Long> entry) {
        return entry.getValue();
    }

    private boolean pruneIds(List<Long> downloadIds, String key) {
        return hasId(downloadIds, keyToId(key));
    }

    private long keyToId(String key) {
        return Long.valueOf(key);
    }

    private boolean hasId(List<Long> downloadIds, long downloadId) {
        for (Long currentId : downloadIds) {
            if (currentId == downloadId) {
                return true;
            }
        }
        return false;
    }

}
