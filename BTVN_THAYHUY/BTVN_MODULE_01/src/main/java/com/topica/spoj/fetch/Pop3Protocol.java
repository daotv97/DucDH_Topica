package com.topica.spoj.fetch;

import javax.inject.Named;

@Named("pop3Protocol")
public class Pop3Protocol implements EmailDownloader {
    private static final String PROTOCOL = "pop3s";
    private static final String HOST_NAME = "pop.gmail.com";
    private static final String PORT = "995";

    public void download(String username, String password) {
        DownloadProvider downloadProvider = new DownloadProvider();
        downloadProvider.downloadEmailAttachments(PROTOCOL, HOST_NAME, PORT, username, password);
    }
}
