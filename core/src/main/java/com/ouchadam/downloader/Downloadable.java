package com.ouchadam.downloader;

import java.net.URL;

public interface Downloadable {

    String title();
    String fileName();
    URL url();

}
