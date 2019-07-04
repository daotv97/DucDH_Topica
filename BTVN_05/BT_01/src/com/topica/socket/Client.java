package com.topica.socket;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private static final String HOST_NAME = "127.0.0.1";
    private static final Integer PORT_NUMBER = 3000;
    private Socket socket = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connection();
        client.send();
        client.close();
    }

    public void connection() throws IOException {
        socket = new Socket(HOST_NAME, PORT_NUMBER);
        System.out.println("Client connected!");
    }

    public void send() throws IOException {
        InputStream inputStream = new FileInputStream("/home/huyduc/Downloads/3h video in 4k relax session - sound and colours of mountain stream, winter without snow.mp4");

        byte[] bytes = new byte[1024];

        OutputStream stream = socket.getOutputStream();

        int count = inputStream.read(bytes, 0, 1024);
        while (count != -1) {
            stream.write(bytes, 0, 1024);
            count = inputStream.read(bytes, 0, 1024);
        }
    }

    public void close() throws IOException {
        if (inputStream != null)
            inputStream.close();
        if (outputStream != null)
            outputStream.close();
        if (socket != null)
            socket.close();
    }
}
