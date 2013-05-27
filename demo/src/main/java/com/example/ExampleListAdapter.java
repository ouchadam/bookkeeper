package com.example;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.ouchadam.bookkeeper.progress.ProgressValues;
import com.ouchadam.bookkeeper.watcher.adapter.DownloadableListAdapter;

import java.util.List;

public class ExampleListAdapter extends DownloadableListAdapter<String, ExampleListAdapter.ViewHolder> {

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
    protected ViewHolder getViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void findViews(View parent, ViewHolder viewHolder) {
        viewHolder.textView = (TextView) parent.findViewById(R.id.text_view);
    }

    @Override
    protected void onStart(ViewHolder viewHolder) {
        viewHolder.textView.setText("Download about to start");
        viewHolder.progressBar.setProgress(0);
        viewHolder.progressBar.setMax(100);
        viewHolder.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onUpdate(ViewHolder viewHolder, ProgressValues progressValues) {
        viewHolder.textView.setText("Downloading...");
        viewHolder.progressBar.setProgress(progressValues.getPercentage());
    }

    @Override
    protected void onStop(ViewHolder viewHolder) {
        viewHolder.textView.setText("Download complete");
        viewHolder.progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onHandleView(Stage stage, int position, ViewHolder viewHolder, String data) {
        if (stage.equals(Stage.IDLE)) {
            viewHolder.textView.setText(data);
        }
    }

    public static class ViewHolder extends DownloadableListAdapter.ProgressHolder {
        private TextView textView;
    }

}