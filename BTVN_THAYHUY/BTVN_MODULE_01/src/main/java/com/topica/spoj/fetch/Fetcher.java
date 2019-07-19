package com.topica.spoj.fetch;

import javax.mail.MessagingException;
import java.io.IOException;

public interface Fetcher {
    void store(ConnectProperties properties) throws MessagingException, IOException;
}
