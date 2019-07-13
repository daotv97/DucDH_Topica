package com.topica.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnector implements ServerService {
    private final Integer port;
    private Socket socket = null;
    private ServerSocket server = null;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;

    public ServerConnector(Integer port) {
        this.port = port;
    }

    public void openServer() throws IOException {
        server = new ServerSocket(port);
        System.out.println("Main is running...");
    }

    public void listening() throws IOException {
        socket = server.accept();
        System.out.println("A new client connected!");
        receiveDataFromClient();
    }

    private void receiveDataFromClient() throws IOException {
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    private void sendDataToClient() throws IOException {

    }

    public void close() throws IOException {
        if (socket != null)
            socket.close();
        if (objectOutputStream != null)
            objectOutputStream.close();
    }
}
