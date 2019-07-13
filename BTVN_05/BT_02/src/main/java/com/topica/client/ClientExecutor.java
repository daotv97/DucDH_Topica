package com.topica.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientExecutor {
    private final String hostName;
    private final Integer port;
    private Socket socket = null;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;

    public ClientExecutor(String hostName, Integer port) {
        this.hostName = hostName;
        this.port = port;
    }

    public void run() throws IOException {
        connection();
        send();
        close();
    }

    private void connection() throws IOException {
        socket = new Socket(hostName, port);
        System.out.println("Successful connection!");
    }

    private void send() throws IOException {
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//        objectOutputStream.writeObject(connection);
    }

    private void close() throws IOException {
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
