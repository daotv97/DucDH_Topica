package com.topica.socket;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
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
        socket = new Socket(Constant.HOST_NAME, Constant.PORT_NUMBER);
        System.out.println("Client connected!");
    }

    public void send() throws IOException {
        inputStream = new FileInputStream(Constant.PATH);
        byte[] bytes = new byte[Constant.LEN_BYTE];
        outputStream = socket.getOutputStream();
        int count = inputStream.read(bytes, Constant.OFF, Constant.LEN_BYTE);
        while (count != -1) {
            outputStream.write(bytes, Constant.OFF, Constant.LEN_BYTE);
            count = inputStream.read(bytes, Constant.OFF, Constant.LEN_BYTE);
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
