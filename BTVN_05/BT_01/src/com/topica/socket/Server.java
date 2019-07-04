package com.topica.socket;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final Integer PORT_NUMBER = 3000;
    private Socket socket = null;
    private ServerSocket server = null;
    private DataOutputStream dataOutputStream = null;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.openServer();
        while (true) {
            Long timeStart = System.currentTimeMillis();
            server.listening();
            Long timeEnd = System.currentTimeMillis();
            System.out.println("time : " + (timeEnd - timeStart));
            server.close();
        }
    }

    public void openServer() throws IOException {
        server = new ServerSocket(PORT_NUMBER);
        System.out.println("Server is running...");
    }

    public void listening() throws IOException {
        socket = server.accept();
        System.out.println("A new client connected!");
        System.out.println("Receiving video...");
        receive();
        System.out.println("Done receiving");
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    private void receive() throws IOException {
        byte[] data = new byte[1024];
        int count = socket.getInputStream().read(data, 0, 1024);
        File video = new File("/home/huyduc/Desktop/client-video.mp4");
        FileOutputStream fos = new FileOutputStream(video);

        while (count != -1) {
            fos.write(data, 0, count);
            count = socket.getInputStream().read(data, 0, 1024);
        }
    }

    public void close() throws IOException { ;
        if (socket != null)
            socket.close();
        if (dataOutputStream != null)
            dataOutputStream.close();
    }
}
