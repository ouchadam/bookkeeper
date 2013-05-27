package com.example;

import android.os.Environment;
import com.ouchadam.bookkeeper.Downloadable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ExampleDownloadable implements Downloadable {

    @Override
    public String title() {
        return "My title";
    }

    @Override
    public File file() {
        return createFile(title());
    }

    private File createFile(String filename) {
        File SDCardRoot = Environment.getExternalStorageDirectory();
        File file = new File(SDCardRoot, filename);
        if (file.exists()) {
            file.delete();
        }
        return file;
    }

    @Override
    public URL url() {
        return getUrl();
    }

    private URL getUrl() {
        try {
            return new URL("http://ipv4.download.thinkbroadband.com/5MB.zip");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Bad url");
        }
    }

}
