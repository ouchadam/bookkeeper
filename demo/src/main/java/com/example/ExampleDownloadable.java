package com.example;

import com.ouchadam.downloader.Downloadable;

import java.net.MalformedURLException;
import java.net.URL;

public class ExampleDownloadable implements Downloadable {

    @Override
    public String title() {
        return "My title";
    }

    @Override
    public String fileName() {
        return "my_file" + System.currentTimeMillis();
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
