package com.topica.client;

import com.topica.utils.Constant;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ClientConnector clientExecutor = new ClientConnector(Constant.HOST_NAME, Constant.PORT_NUMBER);
        try {
            clientExecutor.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
