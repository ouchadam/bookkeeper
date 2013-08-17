package com.ouchadam.bookkeeper.delegate;

import android.content.Context;
import com.ouchadam.bookkeeper.progress.ProgressReceiver;

class ProgressReceiverController {

    private final Context context;
    private final ProgressReceiver progressReceiver;

    private boolean registered;

    ProgressReceiverController(Context context, ProgressReceiver progressReceiver) {
        this.context = context;
        this.progressReceiver = progressReceiver;
    }

    public void register() {
        if (!registered) {
            progressReceiver.register(context.getApplicationContext());
            registered = true;
        }
    }

    public void unregister() {
        if (registered) {
            progressReceiver.unregister(context.getApplicationContext());
            registered = false;
        }
    }
}
