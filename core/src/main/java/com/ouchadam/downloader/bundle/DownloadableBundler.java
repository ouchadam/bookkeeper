package com.ouchadam.downloader.bundle;

import android.os.Bundle;

import com.ouchadam.downloader.Downloadable;

import java.net.URL;

public class DownloadableBundler implements Bundler<Downloadable> {

    private static final String TITLE_KEY = "title_key";
    private static final String FILENAME_KEY = "filename_key";
    private static final String URL_KEY = "url_key";

    @Override
    public Downloadable from(Bundle to) {
        return new UnbundledDownloadable(to.getString(TITLE_KEY), to.getString(FILENAME_KEY), (URL) to.getSerializable(URL_KEY));
    }

    @Override
    public Bundle to(Downloadable from) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEY, from.title());
        bundle.putString(FILENAME_KEY, from.fileName());
        bundle.putSerializable(URL_KEY, from.url());
        return bundle;
    }

    private static class UnbundledDownloadable implements Downloadable {

        private final String title;
        private final String filename;
        private final URL url;

        private UnbundledDownloadable(String title, String filename, URL url) {
            this.title = title;
            this.filename = filename;
            this.url = url;
        }

        @Override
        public String title() {
            return title;
        }

        @Override
        public String fileName() {
            return filename;
        }

        @Override
        public URL url() {
            return url;
        }
    }

}
