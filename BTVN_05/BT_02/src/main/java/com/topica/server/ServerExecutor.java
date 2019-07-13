package com.topica.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerExecutor {
    private final Integer port;
    private Socket socket = null;
    private ServerSocket server = null;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;

    public ServerExecutor(Integer port) {
        this.port = port;
    }

    public void run() throws IOException {
        openServer();
        while (true) {
            listening();
            close();
        }
    }

    public void openServer() throws IOException {
        server = new ServerSocket(port);
        System.out.println("Server is running...");
    }

    public void listening() throws IOException {
        socket = server.accept();
        System.out.println("A new client connected!");
        receive();
    }

    private void receive() throws IOException {
        objectInputStream = new ObjectInputStream(socket.getInputStream());
//        Connection object = (Connection) objectInputStream.readObject();
//        System.out.println(object.getPassword());
    }

    public void close() throws IOException {
        if (socket != null)
            socket.close();
        if (objectOutputStream != null)
            objectOutputStream.close();
    }
}
