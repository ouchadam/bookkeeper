package com.ouchadam.bookkeeper.watcher.notification;

import java.util.ArrayList;
import java.util.List;

class WatchCounter {

    private final List<NotificationDataHolder> notificationDataHolderList;

    public enum DownloadType {
        SINGLE,
        MULTI;
    }
    public WatchCounter() {
        this.notificationDataHolderList = new ArrayList<NotificationDataHolder>();
    }

    public void add(NotificationDataHolder notificationDataHolder) {
        notificationDataHolderList.add(notificationDataHolder);
    }

    public void remove(NotificationDataHolder notificationDataHolder) {
        this.notificationDataHolderList.remove(notificationDataHolder);
    }

    public boolean isEmpty() {
        return notificationDataHolderList.isEmpty();
    }

    public boolean isSingle() {
        return notificationDataHolderList.size() == 1;
    }

    public DownloadType getType() {
        if (isSingle()) {
            return DownloadType.SINGLE;
        } else {
            return DownloadType.MULTI;
        }
    }

    public int getCount() {
        return notificationDataHolderList.size();
    }

    public NotificationDataHolder getLastHolder() {
        return notificationDataHolderList.get(0);
    }
}
