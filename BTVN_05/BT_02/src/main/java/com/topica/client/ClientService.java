package com.topica.client;

import java.io.IOException;

public interface ClientService {
    void connect() throws IOException;

    void terminate();
}
