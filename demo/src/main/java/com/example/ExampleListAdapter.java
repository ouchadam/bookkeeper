package com.example;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class ExampleListAdapter extends DownloadableListAdapter<String, ExampleListAdapter.ProgressHolder> {

    private final int itemLayout;
    private final int progressId;

    public ExampleListAdapter(int itemLayout, int progressId, LayoutInflater layoutInflater, List<String> data) {
        super(layoutInflater, data);
        this.itemLayout = itemLayout;
        this.progressId = progressId;
    }

    @Override
    public int getLayoutId() {
        return itemLayout;
    }

    @Override
    public int getProgressId() {
        return progressId;
    }

    @Override
    protected ProgressHolder getViewHolder() {
        return new ProgressHolder();
    }

    @Override
    protected void findViews(View parent, ProgressHolder viewHolder) {
        super.findViews(parent, viewHolder);
        viewHolder.textView = (TextView) parent.findViewById(R.id.text_view);
    }

    @Override
    protected void onHandleView(ProgressHolder viewHolder, String key) {
        viewHolder.textView.setText(key);
    }

    public static class ProgressHolder extends DownloadableListAdapter.ProgressHolder {
        private TextView textView;
    }

}