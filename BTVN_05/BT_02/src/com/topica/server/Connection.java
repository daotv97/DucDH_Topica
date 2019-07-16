package com.topica.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection extends Thread {
    private Socket socket;
    private String username;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public Connection(Socket socket, String username) {
        this.socket = socket;
        this.username = username;
    }

    @Override
    public void run() {
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            onLog("Thread: " + Thread.currentThread().getName());
            processing();
            close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processing() throws InterruptedException, IOException {
        int response = 1;
        while (response > 0) {
            response = dataInputStream.readInt();
            onLog(new StringBuilder()
                    .append("Receive response data from client-[")
                    .append(username).append("] : ")
                    .append(response)
                    .toString());

            if (response <= 0) continue;
            Thread.sleep(3000);
            dataOutputStream.writeInt(response);
            onLog(new StringBuilder()
                    .append("Send data to client-[")
                    .append(username).append("] : ")
                    .append(response)
                    .toString());
        }
        onLog("[Client - {" + username + "}]: disconnected.");
    }

    private void close() throws IOException {
        if (socket != null) socket.close();
        if (dataOutputStream != null) dataOutputStream.close();
        if (dataInputStream != null) dataInputStream.close();
    }

    public void onLog(String message) {
        System.out.println(message);
    }

    public void onLogError(String message) {
        System.err.println(message);
    }

}
