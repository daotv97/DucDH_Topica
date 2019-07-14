package com.topica.server;

import com.topica.utils.UserAccount;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public class Connection extends Thread {
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Set<UserAccount> userAccountList;

    public Connection(Socket socket, Set<UserAccount> userAccountList) {
        this.socket = socket;
        this.userAccountList = userAccountList;
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
            authenticate();
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void authenticate() throws IOException {
        boolean isExistUser = false;

        while (!isExistUser) {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            String[] account = dataInputStream.readUTF().split("\\:");
            if (userAccountList.stream().anyMatch(userAccount -> userAccount.getUsername().equals(account[0]) && userAccount.getPassword().equals(account[1])))
                isExistUser = true;

            if (isExistUser) {
                onLog("[Client {" + account[0] + "}]: connected.");
                dataOutputStream.writeBoolean(true);
            } else
                dataOutputStream.writeBoolean(false);
        }
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
