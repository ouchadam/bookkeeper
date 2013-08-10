package com.example;

class SimpleItem {
    private final String title;

    private final String url;

    SimpleItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
