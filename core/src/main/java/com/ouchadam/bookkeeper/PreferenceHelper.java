package com.ouchadam.bookkeeper;

import android.content.SharedPreferences;

public class PreferenceHelper {

    private final SharedPreferences sharedPreferences;


    public PreferenceHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void storeDownloadIds(Long[] downloadIds) {
        int counter = 0;
        for (Long downloadId : downloadIds) {
            addId(counter, downloadId);
        }
        addTotal(counter);
    }

    private void addId(int extra, long downloadId) {
        sharedPreferences.edit().putLong("test" + extra, downloadId).apply();
    }

    private void addTotal(int idCount) {
        sharedPreferences.edit().putInt("count", idCount).apply();
    }

    public Long[] getDownloadIds() {
        int count = sharedPreferences.getInt("count", 0);
        Long[] ids = new Long[count];
        for (int i = 0; i < count; i++) {
            ids[i] = sharedPreferences.getLong("test" + i, 0l);
        }
        return ids;
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
