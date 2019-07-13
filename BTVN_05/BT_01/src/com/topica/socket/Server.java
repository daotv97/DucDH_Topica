package com.topica.socket;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private DataOutputStream dataOutputStream = null;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.openServer();
        while (true) {
            server.listening();
            server.close();
        }
    }

    public void openServer() throws IOException {
        serverSocket = new ServerSocket(Constant.PORT_NUMBER);
        System.out.println("Server is running...");
    }

    public void listening() throws IOException {
        socket = serverSocket.accept();
        System.out.println("A new client connected!");
        System.out.println("Receiving video...");
        receive();
        System.out.println("Done receiving");
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    private void receive() throws IOException {
        byte[] data = new byte[Constant.LEN_BYTE];
        int count = socket.getInputStream().read(data, Constant.OFF, Constant.LEN_BYTE);
        File video = new File("/home/huyduc/Desktop/client-video.mp4");
        FileOutputStream fos = new FileOutputStream(video);

        while (count != -1) {
            fos.write(data, Constant.OFF, count);
            count = socket.getInputStream().read(data, Constant.OFF, Constant.LEN_BYTE);
        }
    }

    public void close() throws IOException {
        if (socket != null)
            socket.close();
        if (dataOutputStream != null)
            dataOutputStream.close();
    }
}
