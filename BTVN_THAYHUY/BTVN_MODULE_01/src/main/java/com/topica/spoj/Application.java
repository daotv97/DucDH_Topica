package com.topica.spoj;

import com.topica.spoj.control.DownloadController;

public class Application {
    public static void main(String[] args) {
        DownloadController downloadController = new DownloadController();
        downloadController.downloadAndStoreAttachments();
    }
}
