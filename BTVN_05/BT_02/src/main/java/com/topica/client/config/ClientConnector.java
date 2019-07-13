package com.topica.client.config;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnector implements ClientService {
    private final String hostName;
    private final Integer port;
    private Socket socket = null;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;

    public ClientConnector(String hostName, Integer port) {
        this.hostName = hostName;
        this.port = port;
    }

    public void createConnectToServer() throws IOException {
        socket = new Socket(hostName, port);
        System.out.println("Successful connection!");
    }

    public void handleData() throws IOException {
        sendDataToServer();
    }

    private void sendDataToServer() throws IOException {
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

    }

    private void receiveDataFromServer() {

    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (objectOutputStream != null) {
            objectOutputStream.close();
        }
        if (objectInputStream != null) {
            objectInputStream.close();
        }
    }
}
