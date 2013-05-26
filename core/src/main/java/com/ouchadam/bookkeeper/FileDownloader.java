package com.ouchadam.bookkeeper;

import android.os.Environment;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class FileDownloader {

    private static final int DOWNLOAD_BUFFER_SIZE = 1024;

    private final FileDownloadProgressWatcher progressWatcher;

    private int downloadedSize = 0;
    private int totalSize = 0;
    private HttpURLConnection urlConnection;
    private File file;
    private FileOutputStream fileOutput;
    private InputStream inputStream;
    private String fileName;

    public interface FileDownloadProgressWatcher {
        void onUpdate(ProgressValues progressValues);
    }

    public FileDownloader(FileDownloadProgressWatcher progressWatcher) {
        this.progressWatcher = progressWatcher;
    }

    public void init(URL fileUrl, File file) {
        this.file = file;
        try {
            urlConnection = initConnection(fileUrl);
            fileOutput = getFileOutputStream(file);
            inputStream = urlConnection.getInputStream();
            totalSize = urlConnection.getContentLength();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception in the init");
        }
    }

    private void setFileName(String fileName) {
        validate(fileName);
        this.fileName = fileName;
    }

    private void validate(String fileName) {
        if (fileName == null) {
            throw new NullPointerException("File name cannot be null");
        }
    }

    public void downloadFile() {
        try {
            byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];
            int bufferLength;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                updateProgress();
            }
            fileOutput.close();

        } catch (IOException e) {
            removeFile(file);
            e.printStackTrace();
        }
    }

    private void updateProgress() {
        ProgressValues progressValues = new ProgressValues(downloadedSize, getDownloadedPercentage(downloadedSize), totalSize);
        progressWatcher.onUpdate(progressValues);
    }

    private int getDownloadedPercentage(int downloadedSize) {
        float percent = ((float) downloadedSize / totalSize) * 100;
        return (int) percent;
    }

    private void removeFile(File file) {
        if (file != null) {
            file.delete();
        }
    }

    private FileOutputStream getFileOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    private HttpURLConnection initConnection(URL fileUrl) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) fileUrl.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);

        urlConnection.connect();
        return urlConnection;
    }

}
