package com.example;

import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import com.ouchadam.bookkeeper.progress.ProgressValues;
import com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress;
import com.ouchadam.bookkeeper.watcher.adapter.TypedBaseAdapter;

class ItemProgressManager extends ListItemProgress<SimpleItem, ExampleListAdapter.ViewHolder> {

    public ItemProgressManager(TypedBaseAdapter<SimpleItem> baseAdapter) {
        super(baseAdapter);
    }

    @Override
    protected void onStart(SimpleItem what, ExampleListAdapter.ViewHolder viewHolder) {
        Log.e("!!!", "onStart");
        viewHolder.textView.setText("Download about to start");
        viewHolder.progressBar.setProgress(0);
        viewHolder.progressBar.setMax(100);
        viewHolder.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onUpdate(SimpleItem what, ExampleListAdapter.ViewHolder viewHolder, ProgressValues progressValues) {
        viewHolder.textView.setText("Downloading...");
        viewHolder.progressBar.setVisibility(View.VISIBLE);
        viewHolder.progressBar.setMax(100);
        viewHolder.progressBar.setProgress(progressValues.getPercentage());
    }

    @Override
    protected void onStop(SimpleItem what, ExampleListAdapter.ViewHolder viewHolder) {
        viewHolder.textView.setText("Download complete");
        viewHolder.progressBar.setVisibility(View.GONE);
    }

}
