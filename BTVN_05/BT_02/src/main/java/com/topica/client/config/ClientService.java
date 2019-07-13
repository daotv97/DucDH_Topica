package com.topica.client.config;

import java.io.IOException;

public interface ClientService {
    void createConnectToServer() throws IOException;

    void handleData() throws IOException, ClassNotFoundException;

    void close() throws IOException;
}
