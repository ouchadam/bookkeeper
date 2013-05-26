package com.ouchadam.bookkeeper.bundle;

import android.os.Bundle;

import com.ouchadam.bookkeeper.Downloadable;

import java.io.File;
import java.net.URL;

public class DownloadableBundler implements Bundler<Downloadable> {

    private static final String TITLE_KEY = "title_key";
    private static final String FILE_KEY = "filename_key";
    private static final String URL_KEY = "url_key";

    @Override
    public Downloadable from(Bundle to) {
        return new UnbundledDownloadable(to.getString(TITLE_KEY), (File) to.getSerializable(FILE_KEY), (URL) to.getSerializable(URL_KEY));
    }

    @Override
    public Bundle to(Downloadable from) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEY, from.title());
        bundle.putSerializable(FILE_KEY, from.file());
        bundle.putSerializable(URL_KEY, from.url());
        return bundle;
    }

    private static class UnbundledDownloadable implements Downloadable {

        private final String title;
        private final File file;
        private final URL url;

        private UnbundledDownloadable(String title, File file, URL url) {
            this.title = title;
            this.file = file;
            this.url = url;
        }

        @Override
        public String title() {
            return title;
        }

        @Override
        public File file() {
            return file;
        }

        @Override
        public URL url() {
            return url;
        }
    }

}
