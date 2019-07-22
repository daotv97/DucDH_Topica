package com.topica.spoj.core.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;

public class MailReply {
    private static final Logger LOGGER = Logger.getLogger(MailReply.class.getName());
    private static final String USERNAME = "huyductopica@gmail.com";
    private static final String PASS = "huyducc4";
    private static final String EMAIL_SERVER = "smtp.gmail.com";
    private static final String EMAIL_PORT = "587";
    private static final String STORE = "pop3s";
    private String message;

    public void reply(String message) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", EMAIL_SERVER);
        props.put("mail.smtp.port", EMAIL_PORT);

        try {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);

            Store mailStore = session.getStore(STORE);
            mailStore.connect(EMAIL_SERVER, USERNAME, PASS);

            Folder folder = mailStore.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.getMessages();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(System.in));

            for (int i = 0; i < messages.length; i++) {
                Message emailMessage = messages[i];
                LOGGER.info(String.format("Email %d -", i + 1));
                LOGGER.info("Subject - " + emailMessage.getSubject());
                LOGGER.info("From - " + emailMessage.getFrom()[0]);
            }

            LOGGER.info("Enter email number to " +
                    "which you want to reply: ");
            String emailNo = reader.readLine();

            Message emailMessage = folder.getMessage(Integer.parseInt(emailNo) - 1);

            Message mimeMessage;
            mimeMessage = emailMessage.reply(false);
            mimeMessage.setFrom(new InternetAddress(USERNAME));
            mimeMessage.setText(message);
            mimeMessage.setSubject("RE: " + mimeMessage.getSubject());
            mimeMessage.addRecipient(Message.RecipientType.TO,
                    emailMessage.getFrom()[0]);

            Transport.send(mimeMessage);
            LOGGER.info("Email message " +
                    "replied successfully.");

            folder.close(false);
            mailStore.close();
        } catch (Exception e) {
            LOGGER.info("Error in replying email.");
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(USERNAME, PASS);
        }
    }
}
