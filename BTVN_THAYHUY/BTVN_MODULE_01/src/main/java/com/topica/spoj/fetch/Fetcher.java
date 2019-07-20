package com.topica.spoj.fetch;

import javax.mail.MessagingException;
import java.io.IOException;

public interface Fetcher {
    void fetch(String storeType, String hostname, String port, String username, String password) throws MessagingException, IOException;
}
