package com.topica.server;

import com.topica.server.config.ServerConnector;
import com.topica.utils.Constant;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerConnector serverConnector = new ServerConnector(Constant.PORT_NUMBER);
        serverConnector.openServer();
        serverConnector.listening();
        serverConnector.close();
    }
}
