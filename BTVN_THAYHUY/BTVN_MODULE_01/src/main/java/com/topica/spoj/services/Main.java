package com.topica.spoj.services;

import com.topica.spoj.fetch.ImapFetchingEmail;

import javax.mail.MessagingException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, MessagingException {
        ImapFetchingEmail imapFetchingEmail = new ImapFetchingEmail();
        String host = "imap.googlemail.com";
        String storeName = "imaps";
        String mailStoreType = "imap";
        String username = "ittalent@topica.edu.vn";
        String password = "topica123";
    }
}
