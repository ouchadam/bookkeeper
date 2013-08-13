package com.ouchadam.bookkeeper;

public class DownloadId {

    private final long value;

    public DownloadId(long downloadId) {
        this.value = downloadId;
    }

    public long value() {
        return value;
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
}
