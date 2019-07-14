package com.topica.server;

import com.topica.utils.Constant;
import com.topica.utils.UserAccount;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<UserAccount> userAccountList = new HashSet<>();
        userAccountList = initUserAccount(userAccountList);
        ServerConnector serverConnector = new ServerConnector(Constant.PORT_NUMBER, userAccountList);
        try {
            serverConnector.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<UserAccount> initUserAccount(Set<UserAccount> userAccountList) {
        userAccountList.add(new UserAccount("user1", "12345"));
        userAccountList.add(new UserAccount("user2", "12345"));
        userAccountList.add(new UserAccount("user3", "12345"));
        userAccountList.add(new UserAccount("user4", "12345"));
        userAccountList.add(new UserAccount("user5", "12345"));
        return userAccountList;
    }
}
