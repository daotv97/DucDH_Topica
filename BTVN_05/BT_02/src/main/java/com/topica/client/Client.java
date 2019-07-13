package com.topica.client;

import com.topica.utils.Constant;

import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        ClientExecutor clientExecutor = new ClientExecutor(Constant.HOST_NAME, Constant.PORT_NUMBER);
        try {
            clientExecutor.run();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
