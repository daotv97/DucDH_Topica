package com.topica.imap;

import javax.mail.MessagingException;
import java.io.IOException;

public interface Fetcher {
    void fetch() throws NoSuchFieldException, IOException, MessagingException;

    void fetch(String storeType, String nameStore, String host, String username, String password) throws MessagingException;
}
