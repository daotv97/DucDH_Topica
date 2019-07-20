package com.topica.spoj.fetch;

import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import java.io.IOException;
import java.util.Properties;

class DownloadProvider {

    private static final Logger LOGGER = Logger.getLogger(DownloadProvider.class);

    /**
     * Returns a properties object which is configured for a POP3/IMAP server
     *
     * @param protocol either "imap" or "pop3"
     * @param hostname
     * @param port
     * @param username
     * @param password
     * @return a properties object
     */
    private Properties getServerProperties(String protocol, String hostname, String port, String username, String password) {
        Properties properties = new Properties();
        // server setting
        properties.put(String.format(Constant.HOST_NAME_PROPERTY, protocol), hostname);
        properties.put(String.format(Constant.PORT_PROPERTY, protocol), port);
        properties.put(String.format(Constant.USERNAME_PROPERTY, protocol), username);
        properties.put(String.format(Constant.PASS_PROPERTY, protocol), password);
        properties.put(String.format(Constant.START_TLS_PROPERTY, protocol), Constant.START_TLS_VALUE);

        // SSL setting
        properties.setProperty(String.format(Constant.SOCKET_CLASS_PROPERTY, protocol), Constant.SOCKET_CLASS_VALUE);
        properties.setProperty(String.format(Constant.SOCKET_FALLBACK_PROPERTY, protocol), Constant.SOCKET_FALLBACK_VALUE);
        properties.setProperty(String.format(Constant.SOCKET_FALLBACK_PORT, protocol), port);

        return properties;
    }

    /**
     * Connects to the message store
     *
     * @param session
     * @param protocol
     * @param hostname
     * @param username
     * @param password
     * @return a message store
     * @throws MessagingException
     */
    private Store connect(Session session, String protocol, String hostname, String username, String password) throws MessagingException {
        Store store = session.getStore(protocol);
        store.connect(hostname, username, password);
        return store;
    }

    /**
     * Fetches new messages from server
     *
     * @param folder
     * @throws MessagingException
     * @throws IOException
     */
    private void fetchNewMessages(Folder folder) throws MessagingException, IOException {
        Message[] messages = folder.getMessages();
        for (int i = 0; i < messages.length; i++) {
            messageInfo(messages[i], i);
        }
    }

    /**
     * Info messages
     *
     * @param message
     * @param index
     * @throws MessagingException
     * @throws IOException
     */
    private void messageInfo(Message message, int index) throws MessagingException, IOException {
        Address[] fromAddress = message.getFrom();
        LOGGER.info("== Email #" + (index + 1) + ": ====================================================");
        LOGGER.info("\t Subject: " + message.getSubject());
        LOGGER.info("\t From: " + fromAddress[0].toString());
        LOGGER.info("\t To: " + parseAddresses(message.getRecipients(RecipientType.TO)));
        LOGGER.info("\t CC: " + parseAddresses(message.getRecipients(RecipientType.CC)));
        LOGGER.info("\t Sent Date: " + message.getSentDate().toString());
        LOGGER.info("\t Message: " + message.getContent());
        LOGGER.info("================================================================\n");
    }

    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    private String parseAddresses(Address[] address) {
        String listAddress = "";
        if (address != null) {
            StringBuilder listAddressBuilder = new StringBuilder();
            for (Address value : address) {
                listAddressBuilder.append(value.toString()).append(", ");
            }
            listAddress = listAddressBuilder.toString();
        }
        if (listAddress.length() <= 1) {
            return listAddress;
        }
        listAddress = listAddress.substring(0, listAddress.length() - 2);
        return listAddress;
    }

    /**
     * Downloads new messages and fetches details for each message.
     *
     * @param protocol
     * @param hostname
     * @param port
     * @param username
     * @param password
     */
    void downloadEmails(String protocol, String hostname, String port, String username, String password) {
        LOGGER.debug("Info: {" +
                "protocol: " +
                protocol +
                ", hostname: " +
                hostname +
                ", port: " +
                port +
                ", username: " +
                username +
                ", password: " +
                password +
                "}");
        Properties properties = getServerProperties(protocol, hostname, port, username, password);
        Session session = Session.getDefaultInstance(properties);
        try {
            Store store = connect(session, protocol, hostname, username, password);
            Folder folderInbox = store.getFolder(Constant.FOLDER);
            folderInbox.open(Folder.READ_ONLY);
            fetchNewMessages(folderInbox);
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            LOGGER.error("No provider for protocol: " + protocol);
        } catch (MessagingException ex) {
            LOGGER.error("Could not connect to the message store");
        } catch (IOException ex) {
            LOGGER.error("[Error downloading content]");
        }
    }
}
