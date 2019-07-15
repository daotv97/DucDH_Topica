package com.topica.server;

import com.topica.utils.UserAccount;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public class Connection extends Thread {
    private Socket socket;
    private String username;
    private boolean terminate;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public Connection(Socket socket, Set<UserAccount> userAccountList) {
        this.socket = socket;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            onLog("Thread: " + Thread.currentThread().getName());
            listening();
            close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void listening() throws InterruptedException, IOException {
        int response = 1;
        while (response > 0) {
            response = dataInputStream.readInt();
            onLog(new StringBuilder()
                    .append("Receive response data from client-[")
                    .append(username).append("] : ")
                    .append(response)
                    .toString());

            if (response <= 0) {
                continue;
            }
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
        if (socket != null)
            socket.close();
        if (dataOutputStream != null)
            dataOutputStream.close();
        if (dataInputStream != null)
            dataInputStream.close();
    }

    public void onLog(String message) {
        System.out.println(message);
    }

    public void onLogError(String message) {
        System.err.println(message);
    }

}
