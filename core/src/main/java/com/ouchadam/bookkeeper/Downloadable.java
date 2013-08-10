package com.ouchadam.bookkeeper;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

public interface  Downloadable {
    String title();
    String description();
    String fileName();
    Uri url();
}
