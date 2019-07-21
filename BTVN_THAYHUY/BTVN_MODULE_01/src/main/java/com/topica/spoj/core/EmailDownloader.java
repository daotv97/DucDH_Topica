package com.topica.spoj.core;

public interface EmailDownloader {
    void download(String username, String password, String subject, String expired);
}
