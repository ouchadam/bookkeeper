package com.ouchadam.bookkeeper.progress;

import android.content.Intent;
import com.ouchadam.bookkeeper.domain.DownloadId;

class DownloadIdFactory {

    DownloadId from(Intent intent) {
        if (intent.hasExtra(ProgressUpdater.EXTRA_DOWNLOAD_ID)) {
            return new DownloadId(safeGetLong(intent, ProgressUpdater.EXTRA_DOWNLOAD_ID));
        } else {
            return DownloadId.invalid();
        }
    }

    private long safeGetLong(Intent intent, String key) {
        long missingLong = -100L;
        long longExtra = intent.getLongExtra(key, missingLong);
        if (longExtra == 100L) {
            throw new RuntimeException(ProgressUpdater.EXTRA_DOWNLOAD_ID + " was somehow missing, this should never ever happen");
        }
        return longExtra;
    }

}
