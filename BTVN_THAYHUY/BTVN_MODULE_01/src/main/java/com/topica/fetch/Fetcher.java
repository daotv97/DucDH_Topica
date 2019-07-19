package com.topica.fetch;

import javax.mail.MessagingException;

public interface Fetcher {
    void fetch(String storeType, String nameStore, String hostname, String username, String password) throws MessagingException;
}
