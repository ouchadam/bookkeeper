package com.ouchadam.bookkeeper.delegate;

class FileNameEnforcer {

    public String enforce(String filename) {
        return filename.replace('/', '_');
    }

}
