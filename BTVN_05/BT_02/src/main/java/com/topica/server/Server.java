package com.topica.server;

import com.topica.utils.Constant;

import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        ServerExecutor serverExecutor = new ServerExecutor(Constant.PORT_NUMBER);
        try {
            serverExecutor.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
