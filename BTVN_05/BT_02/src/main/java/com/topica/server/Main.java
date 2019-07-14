package com.topica.server;

import com.topica.utils.Constant;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ServerConnector serverConnector = new ServerConnector(Constant.PORT_NUMBER);
        try {
            serverConnector.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
