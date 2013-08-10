package com.ouchadam.bookkeeper.watcher.adapter;

import android.widget.BaseAdapter;
import com.ouchadam.bookkeeper.progress.ProgressValues;

import java.util.HashMap;
import java.util.Map;

public abstract class ListItemProgress<T, VH> implements ProgressDelegate<VH> {

    private final Map<Long, ProgressValues> progressValues;
    private final Map<Long, Stage> stageValues;
    private TypedBaseAdapter<T> baseAdapter;

    public enum Stage {
        IDLE,
        START,
        UPDATING,
        STOP,
    }

    public ListItemProgress(TypedBaseAdapter<T> baseAdapter) {
        this.baseAdapter = baseAdapter;
        this.stageValues = new HashMap<Long, Stage>();
        this.progressValues = new HashMap<Long, ProgressValues>();
    }

    @Override
    public void handleDownloadProgress(int position, VH viewHolder) {
        if (stageValues.containsKey(getItemId(position))) {
            Stage stage = stageValues.get(getItemId(position));
            T data = getData(position);
            switch (stage) {
                case START:
                    onStart(data, viewHolder);
                    break;
                case UPDATING:
                    onUpdate(data, viewHolder, progressValues.get(getItemId(position)));
                    break;
                case STOP:
                    progressValues.remove(getItemId(position));
                    onStop(data, viewHolder);
                    break;
                default:
                    break;
            }
        }
    }

    private T getData(int position) {
        return baseAdapter.getItem(position);
    }

    protected abstract void onStart(T what, VH viewHolder);

    protected abstract void onUpdate(T what, VH viewHolder, ProgressValues progressValues);

    protected abstract void onStop(T what, VH viewHolder);

    @Override
    public Stage getStage(int position) {
        if (stageValues.containsKey(getItemId(position))) {
            return stageValues.get(getItemId(position));
        }
        return Stage.IDLE;
    }

    private long getItemId(int position) {
        return baseAdapter.getItemId(position);
    }

    @Override
    public void setStageFor(long itemId, Stage stage) {
        this.stageValues.put(itemId, stage);
    }

    @Override
    public void updateProgressValuesFor(long itemId, ProgressValues progressValues) {
        this.progressValues.put(itemId, progressValues);
    }

}
