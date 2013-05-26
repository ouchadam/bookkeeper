package com.ouchadam.bookkeeper;

import java.io.Serializable;

public class ProgressValues implements Serializable {

    private final int downloadedSize;
    private int percentage;
    private final int totalSize;

    public ProgressValues(int downloadedSize, int downloadedPercentage, int totalSize) {
        this.downloadedSize = downloadedSize;
        percentage = downloadedPercentage;
        this.totalSize = totalSize;
    }

    public int getPercentage() {
        return percentage;
    }

    public int getTotal() {
        return totalSize;
    }

    public int getDownloaded() {
        return downloadedSize;
    }
}
