package com.ouchadam.bookkeeper.domain;

import java.io.Serializable;

public class DownloadId implements Serializable {

    private static final long INVALID_ID_VALUE = -1L;
    private static final DownloadId INVALID_ID = new DownloadId(INVALID_ID_VALUE);

    private final long value;

    public DownloadId(long downloadId) {
        this.value = downloadId;
    }

    public String toKey() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadId that = (DownloadId) o;
        if (value != that.value) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    public static DownloadId invalid() {
        return INVALID_ID;
    }

    public long value() {
        return value;
    }

    public boolean isValid() {
        return this.value != INVALID_ID_VALUE;
    }
}
