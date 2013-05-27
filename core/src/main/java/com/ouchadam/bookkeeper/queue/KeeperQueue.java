package com.ouchadam.bookkeeper.queue;

import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.Downloadable;

import java.util.List;
import java.util.Stack;

public class KeeperQueue {

    private final Stack<QueuedKeep> queue;

    public KeeperQueue() {
        this.queue = new Stack<QueuedKeep>();
    }

    public void push(Downloadable downloadable, List<DownloadWatcher> downloadWatcherList) {
        queue.push(new QueuedKeep(downloadable, downloadWatcherList));
    }

    public QueuedKeep pop() {
        return queue.pop();
    }

    public boolean hasNext() {
        return !queue.isEmpty();
    }

    public static class QueuedKeep {

        private final Downloadable downloadable;
        private final List<DownloadWatcher> downloadWatchers;

        public QueuedKeep(Downloadable downloadable, List<DownloadWatcher> downloadWatchers) {
            this.downloadable = downloadable;
            this.downloadWatchers = downloadWatchers;
        }

        public Downloadable getDownloadable() {
            return downloadable;
        }

        public List<DownloadWatcher> getDownloadWatchers() {
            return downloadWatchers;
        }

    }

}
