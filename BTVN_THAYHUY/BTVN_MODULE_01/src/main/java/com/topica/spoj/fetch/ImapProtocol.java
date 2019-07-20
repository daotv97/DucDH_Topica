package com.topica.spoj.fetch;

import javax.inject.Named;

@Named("imapProtocol")
public class ImapProtocol implements EmailDownloader {
    private static final String PROTOCOL = "imap";
    private static final String HOST_NAME = "imap.googlemail.com";
    private static final String PORT = "993";

    public void download(String username, String password) {
        DownloadProvider downloadProvider = new DownloadProvider();
        downloadProvider.downloadEmails(PROTOCOL, HOST_NAME, PORT, username, password);
    }
}