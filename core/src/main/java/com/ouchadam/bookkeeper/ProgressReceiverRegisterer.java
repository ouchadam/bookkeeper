package com.ouchadam.bookkeeper;

import android.content.Context;
import com.ouchadam.bookkeeper.progress.ProgressReceiver;

class ProgressReceiverRegisterer {

    private final Context context;
    private final ProgressReceiver progressReceiver;

    private boolean registered;

    ProgressReceiverRegisterer(Context context, ProgressReceiver progressReceiver) {
        this.context = context;
        this.progressReceiver = progressReceiver;
    }

    public void register() {
        if (!registered) {
            registered = true;
            progressReceiver.register(context);
        }
    }

    public void unregister() {
        if (registered) {
            progressReceiver.unregister(context);
        }
    }
}
