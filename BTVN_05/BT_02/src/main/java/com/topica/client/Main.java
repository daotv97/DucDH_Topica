package com.topica.client;

import com.topica.client.config.ClientConnector;
import com.topica.utils.Constant;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ClientConnector clientExecutor = new ClientConnector(Constant.HOST_NAME, Constant.PORT_NUMBER);
        clientExecutor.createConnectToServer();
        clientExecutor.handleData();
        clientExecutor.close();
    }
}
