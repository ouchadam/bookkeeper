package com.example;

import android.net.Uri;
import android.os.Environment;
import com.ouchadam.bookkeeper.Downloadable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class ExampleDownloadable implements Downloadable {

    private final SimpleItem simpleItem;

    public ExampleDownloadable(SimpleItem simpleItem) {
        this.simpleItem = simpleItem;
    }

    @Override
    public String title() {
        return simpleItem.getTitle();
    }

    @Override
    public String description() {
        return simpleItem.getTitle();
    }

    @Override
    public String fileName() {
        return title();
    }

    @Override
    public Uri url() {
        return Uri.parse(simpleItem.getUrl());
    }

}
