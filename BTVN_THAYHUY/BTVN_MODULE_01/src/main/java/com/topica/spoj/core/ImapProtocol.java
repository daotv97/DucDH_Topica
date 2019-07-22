package com.topica.spoj.core;

import com.topica.spoj.core.store.DownloadProvider;

import javax.inject.Named;

@Named("imapProtocol")
public class ImapProtocol implements EmailDownloader {
    private static final String PROTOCOL = "imap";
    private static final String HOST_NAME = "imap.gmail.com";
    private static final String PORT = "993";

    @Override
    public void download(String username, String password, String subject, String expired) {
        DownloadProvider downloadProvider = new DownloadProvider();
        downloadProvider.downloadEmailAttachments(PROTOCOL, HOST_NAME, PORT, username, password, subject, expired);
    }
}
