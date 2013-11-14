package com.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ouchadam.bookkeeper.domain.ProgressValues;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.bookkeeper.watcher.adapter.ProgressDelegate;
import com.ouchadam.bookkeeper.watcher.adapter.TypedBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress.Stage;

public class ExampleListAdapter extends TypedBaseAdapter<SimpleItem> implements ListItemWatcher.ItemWatcher {

    private final LayoutInflater layoutInflater;
    private final List<SimpleItem> data;
    private final ProgressDelegate<ViewHolder> progressDelegate;

    public ExampleListAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        this.data = createAdapterData();
        this.progressDelegate = new ItemProgressManager(this);
    }

    private static List<SimpleItem> createAdapterData() {
        List<SimpleItem> data = new ArrayList<SimpleItem>();
        data.add(new SimpleItem("5mb", "http://ipv4.download.thinkbroadband.com/5MB.zip"));
        data.add(new SimpleItem("200mb", "http://download.thinkbroadband.com/200MB.zip"));
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public SimpleItem getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.get(i).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = createView(convertView, viewGroup);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        progressDelegate.handleDownloadProgress(position, viewHolder);
        Stage stage = progressDelegate.getStage(position);
        if (stage == Stage.IDLE) {
            viewHolder.textView.setText(getItem(position).getTitle());
        }
        return view;
    }

    private View createView(View convertView, ViewGroup viewGroup) {
        return convertView == null ? createNewView(viewGroup) : convertView;
    }

    private View createNewView(ViewGroup viewGroup) {
        View newView = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        initViewTag(newView);
        return newView;
    }

    private void initViewTag(View newView) {
        ViewHolder tag = new ViewHolder();
        initHolder(newView, tag);
        newView.setTag(tag);
    }

    private void initHolder(View newView, ViewHolder tag) {
        tag.textView = (TextView) newView.findViewById(R.id.text_view);
        tag.progressBar = (ProgressBar) newView.findViewById(R.id.list_item_progress_bar);
    }

    @Override
    public void setStageFor(long itemId, Stage stage) {
        progressDelegate.setStageFor(itemId, stage);
    }

    @Override
    public void updateProgressValuesFor(long itemId, ProgressValues progressValues) {
        progressDelegate.updateProgressValuesFor(itemId, progressValues);
    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView textView;
        ProgressBar progressBar;
    }

}