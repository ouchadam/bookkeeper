package com.ouchadam.bookkeeper.watcher.adapter;

import com.ouchadam.bookkeeper.domain.ProgressValues;

public interface ProgressDelegate<VH> {
    ListItemProgress.Stage getStage(int position);
    void setStageFor(long itemId, ListItemProgress.Stage stage);
    void updateProgressValuesFor(long itemId, ProgressValues progressValues);
    void handleDownloadProgress(int position, VH viewHolder);
}
