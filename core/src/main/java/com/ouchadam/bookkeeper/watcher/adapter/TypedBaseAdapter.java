package com.ouchadam.bookkeeper.watcher.adapter;

import android.widget.BaseAdapter;

public abstract class TypedBaseAdapter<T> extends BaseAdapter {

    @Override
    public abstract T getItem(int i);

}
