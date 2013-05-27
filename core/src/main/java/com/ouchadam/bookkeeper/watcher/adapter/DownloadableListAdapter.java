package com.ouchadam.bookkeeper.watcher.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import com.ouchadam.bookkeeper.progress.ProgressValues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ouchadam.bookkeeper.watcher.adapter.DownloadableListAdapter.*;

public abstract class DownloadableListAdapter<T, VH extends ProgressHolder> extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final List<T> what;
    private Map<Long, ProgressValues> progressValues;
    private Map<Long, Stage> stageValues;

    public enum Stage {
        IDLE,
        START,
        UPDATING,
        STOP,
    }

    public DownloadableListAdapter(LayoutInflater layoutInflater, List<T> data) {
        this.layoutInflater = layoutInflater;
        this.what = data;
        this.stageValues = new HashMap<Long, Stage>();
        this.progressValues = new HashMap<Long, ProgressValues>();
    }

    @Override
    public int getCount() {
        return what.size();
    }

    @Override
    public T getItem(int i) {
        return what.get(i);
    }

    @Override
    public long getItemId(int i) {
        return what.get(i).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = getView(convertView);
        VH viewHolder = (VH) view.getTag();
        T data = what.get(position);
        handleDownloadProgress(position, viewHolder);
        onHandleView(getStage(position), position, viewHolder, data);
        return view;
    }

    private View getView(View convertView) {
        return convertView == null ? createView() : convertView;
    }

    private View createView() {
        View view = layoutInflater.inflate(getLayoutId(), null, false);
        VH viewHolder = getViewHolder();
        findViews(view, viewHolder);
        viewHolder.progressBar = (ProgressBar) view.findViewById(getProgressId());
        view.setTag(viewHolder);
        return view;
    }

    protected abstract VH getViewHolder();

    protected abstract int getLayoutId();

    protected abstract int getProgressId();

    private void handleDownloadProgress(int position, VH viewHolder) {
        if (stageValues.containsKey(getItemId(position))) {
            Stage stage = stageValues.get(getItemId(position));
            switch (stage) {
                case START:
                    onStart(viewHolder);
                    break;
                case UPDATING:
                    onUpdate(viewHolder, progressValues.get(getItemId(position)));
                    break;
                case STOP:
                    progressValues.remove(getItemId(position));
                    onStop(viewHolder);
                    break;
                default:
                    break;
            }
        }
    }

    protected abstract void onStart(VH viewHolder);

    protected abstract void onUpdate(VH viewHolder, ProgressValues progressValues);

    protected abstract void onStop(VH viewHolder);

    protected void onHandleView(Stage stage, int position, VH viewHolder, T data) {
    }

    private Stage getStage(int position) {
        if (stageValues.containsKey(getItemId(position))) {
            return stageValues.get(getItemId(position));
        }
        return Stage.IDLE;
    }

    protected abstract void findViews(View parent, VH viewHolder);

    public void setStageFor(long itemId, Stage stage) {
        this.stageValues.put(itemId, stage);
    }

    public void updateProgressValuesFor(long itemId, ProgressValues progressValues) {
        this.progressValues.put(itemId, progressValues);
    }

    public static abstract class ProgressHolder {
        public ProgressBar progressBar;
    }

}
