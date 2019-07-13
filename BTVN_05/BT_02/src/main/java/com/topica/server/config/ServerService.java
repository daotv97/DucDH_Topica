package com.topica.server.config;

import java.io.IOException;

public interface ServerService {
    void openServer() throws IOException;

    void listening() throws IOException;

    void close() throws IOException;
}
