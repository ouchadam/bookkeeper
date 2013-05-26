package com.ouchadam.bookkeeper;

import java.io.File;
import java.net.URL;

public interface Downloadable {
    String title();
    File file();
    URL url();
}
