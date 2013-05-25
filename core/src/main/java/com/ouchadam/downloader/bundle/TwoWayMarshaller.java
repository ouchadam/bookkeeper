package com.ouchadam.downloader.bundle;

public interface TwoWayMarshaller<T, F> {
    F from(T to);
    T to(F from);
}
