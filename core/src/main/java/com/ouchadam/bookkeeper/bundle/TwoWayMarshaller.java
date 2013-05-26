package com.ouchadam.bookkeeper.bundle;

public interface TwoWayMarshaller<T, F> {
    F from(T to);
    T to(F from);
}
