package com.topica.spoj.fetch;

import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

class DownloadProvider {

    private static final Logger LOGGER = Logger.getLogger(DownloadProvider.class.getName());

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
    private void fetchNewMessages(Folder folder, FlagTerm flagTerm, String subject, String expired) throws MessagingException, IOException {
        Message[] messages = folder.search(flagTerm);
        for (int i = 0; i < messages.length; i++) {
            if (isValidMessage(messages[i], subject, expired)) {
                messageInfo(messages[i], i);
                messages[i].setFlag(Flags.Flag.SEEN, true);
            }
        }
    }

    private void getAttachments(Message message, String subjectRegex) throws MessagingException {
        String contentType = message.getContentType();
        Address from = message.getFrom()[0];

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
    void downloadEmailAttachments(String protocol, String hostname, String port, String username, String password, String subject, String expired) {
        LOGGER.debug(String.format("Info: '{'protocol: %s, hostname: %s, port: %s, username: %s, password: %s'}'", protocol, hostname, port, username, password));
        Properties properties = getServerProperties(protocol, hostname, port, username, password);
        Session session = Session.getDefaultInstance(properties);
        try {
            Store store = connect(session, protocol, hostname, username, password);
            Folder folderInbox = store.getFolder(Constant.FOLDER);
            folderInbox.open(Folder.READ_WRITE);

            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, Constant.FLAG_TERM);
            fetchNewMessages(folderInbox, unseenFlagTerm, subject, expired);

            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            LOGGER.error(String.format("No provider for protocol: %s", protocol));
        } catch (MessagingException ex) {
            LOGGER.error("Could not connect to the message store.");
        } catch (IOException e) {
            LOGGER.error("Load content failed.");
        }
    }

    /**
     * Check valid messages
     *
     * @param message
     * @param subject
     * @param expired
     * @return
     * @throws MessagingException
     */
    private boolean isValidMessage(Message message, String subject, String expired) throws MessagingException {
        return message.getSubject().startsWith(subject) && message.getContentType().contains(Constant.CONTENT_TYPE_MULTIPART) && isExpiredMessage(message.getSentDate(), expired);
    }

    /**
     * Check the expiry date of the homework
     *
     * @param sendDate
     * @param expired
     * @return
     */
    private boolean isExpiredMessage(Date sendDate, String expired) {
        Date dateExpired;
        try {
            dateExpired = new SimpleDateFormat("dd/MM/yyyy").parse(expired);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        LOGGER.debug("Send date: " + sendDate.toString());
        LOGGER.debug("Expired date: " + dateExpired.toString());
        return sendDate.getTime() < dateExpired.getTime();
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
        LOGGER.info(String.format("== Email #%d: ====================================================", index + 1));
        LOGGER.info(String.format("\t Subject: %s", message.getSubject()));
        LOGGER.info(String.format("\t From: %s", fromAddress[0].toString()));
        LOGGER.info(String.format("\t To: %s", parseAddresses(message.getRecipients(RecipientType.TO))));
        LOGGER.info(String.format("\t CC: %s", parseAddresses(message.getRecipients(RecipientType.CC))));
        LOGGER.info(String.format("\t Sent Date: %s", message.getSentDate().toString()));
        LOGGER.info(String.format("\t Message: %s", message.getContent()));
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
}
