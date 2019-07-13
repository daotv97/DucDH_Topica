package com.topica.client;

import java.io.IOException;

public interface ClientService {
    void createConnect() throws IOException;

    void connection() throws IOException;

    void close() throws IOException;
}
