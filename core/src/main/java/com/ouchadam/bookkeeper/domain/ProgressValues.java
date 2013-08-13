package com.ouchadam.bookkeeper.domain;

import java.io.Serializable;

public class ProgressValues implements Serializable {

    private static final int PERCENTAGE_MULTIPLIER = 100;

    private final int downloadedSize;
    private int percentage;
    private final int totalSize;

    public ProgressValues(int downloadedSize, int totalSize) {
        this.downloadedSize = downloadedSize;
        this.percentage = createDownloadedPercentage(downloadedSize, totalSize);
        this.totalSize = totalSize;
    }

    private static int createDownloadedPercentage(float downloadedSize, int totalSize) {
        float percent = (downloadedSize / (float) totalSize) * PERCENTAGE_MULTIPLIER;
        return (int) percent;
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
