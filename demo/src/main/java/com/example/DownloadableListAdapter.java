package com.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.DownloadableListAdapter.*;

public abstract class DownloadableListAdapter<T, V extends ProgressHolder> extends BaseAdapter {

    private final Map<T, ProgressBar> itemList;
    private final LayoutInflater layoutInflater;
    private final List<T> data;

    public DownloadableListAdapter(LayoutInflater layoutInflater, List<T> data) {
        this.layoutInflater = layoutInflater;
        this.data = data;
        itemList = new HashMap<T, ProgressBar>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.get(i).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;
        if (convertView == null) {
            view = createView();
        } else {
            view = convertView;
        }
        V viewHolder = (V) view.getTag();
        T key = data.get(position);
        itemList.put(key, viewHolder.progressBar);
        onHandleView(viewHolder, key);
        return view;
    }

    protected void onHandleView(V viewHolder, T key) {
    }

    private View createView() {
        View view = layoutInflater.inflate(getLayoutId(), null, false);
        V viewHolder = getViewHolder();
        findViews(view, viewHolder);
        viewHolder.progressBar = (ProgressBar) view.findViewById(getProgressId());
        view.setTag(viewHolder);
        return view;
    }

    protected abstract int getLayoutId();

    protected abstract int getProgressId();

    protected abstract V getViewHolder();

    protected void findViews(View parent, V viewHolder) {
    }

    protected static abstract class ProgressHolder {
        protected ProgressBar progressBar;
    }

    protected ProgressBar getProgressBarFrom(int position) {
        return itemList.get(getItem(position));
    }

}
