package com.topica.spoj.core;

import com.topica.spoj.core.store.DownloadProvider;

import javax.inject.Named;

@Named("pop3Protocol")
public class Pop3Protocol implements EmailDownloader {
    private static final String PROTOCOL = "pop3s";
    private static final String HOST_NAME = "pop.gmail.com";
    private static final String PORT = "995";

    @Override
    public void download(String username, String password, String subject, String expired) {
        DownloadProvider downloadProvider = new DownloadProvider();
        downloadProvider.downloadEmailAttachments(PROTOCOL, HOST_NAME, PORT, username, password, subject, expired);
    }
}