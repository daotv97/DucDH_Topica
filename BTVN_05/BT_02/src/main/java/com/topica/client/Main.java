package com.topica.client;

import com.topica.utils.Constant;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ClientConnector clientExecutor = new ClientConnector(Constant.HOST_NAME, Constant.PORT_NUMBER);
        clientExecutor.createConnect();
        clientExecutor.connection();
        clientExecutor.close();
    }
}
