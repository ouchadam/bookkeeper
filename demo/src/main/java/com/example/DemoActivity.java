package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ouchadam.downloader.Downloadable;
import com.ouchadam.downloader.Downloader;
import com.ouchadam.downloader.watcher.NotificationWatcher;

import java.net.MalformedURLException;
import java.net.URL;

public class DemoActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.download_start).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Downloadable test = new Test();

        NotificationWatcher notificationWatcher = new NotificationWatcher(this);

        Downloader.download(this, test, notificationWatcher);
    }

    private static class Test implements Downloadable {

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
                e.printStackTrace();
            }
            return null;
        }

    }

}

