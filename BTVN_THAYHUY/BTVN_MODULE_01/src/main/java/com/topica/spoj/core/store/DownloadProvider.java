package com.topica.spoj.core.store;

import com.topica.spoj.core.exception.FileStorageException;
import com.topica.spoj.core.utils.FileHandler;
import com.topica.spoj.core.utils.MailReply;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DownloadProvider {

    private static final Logger LOGGER = Logger.getLogger(DownloadProvider.class.getName());
    private static final Path ROOT_PATCH = Paths.get(System.getProperty("user.home") + "/homework");
    private MailReply mailReply = new MailReply();

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
     * @param flagTerm
     * @param username
     * @param subject
     * @param expired
     * @throws MessagingException
     * @throws IOException
     * @throws FileStorageException
     */
    private void fetchNewMessages(Folder folder, FlagTerm flagTerm, String username, String subject, String expired) throws MessagingException, IOException, FileStorageException, InterruptedException {
        Message[] messages = folder.search(flagTerm);
        for (int i = 0; i < messages.length; i++) {
            if (isValidMessage(messages[i], subject, expired)) {
                messageInfo(messages[i], i);
                getAttachments(messages[i], username, subject);
                messages[i].setFlag(Flags.Flag.SEEN, true);
            }
        }
    }

    /**
     * Get Attachments from messages.
     *
     * @param message
     * @param username
     * @param subject
     * @throws IOException
     * @throws MessagingException
     * @throws FileStorageException
     */
    private void getAttachments(Message message, String username, String subject) throws IOException, MessagingException, FileStorageException, InterruptedException {
        Multipart multiPart = (Multipart) message.getContent();
        int numberOfParts = multiPart.getCount();
        boolean isFileZip = false;
        String dir = String.format("%s/%s/%s/%s", ROOT_PATCH, username, subject, ((InternetAddress) message.getFrom()[0]).getAddress());

        for (int partCount = 0; partCount < numberOfParts; partCount++) {
            BodyPart part = multiPart.getBodyPart(partCount);
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                String fileName = part.getFileName();
                Optional<String> extension = getFileExtension(fileName);

                if (extension.isPresent() && extension.get().equals(Constant.ZIP)) {
                    isFileZip = true;

                    String pathFileUnzip = MessageFormat.format("{0}/{1}", dir, Constant.UNZIP);

                    handleAttachment(part, dir, pathFileUnzip, fileName);
                }
            }
        }
        if (!isFileZip) {
            LOGGER.error(Constant.MESSAGE_FILE_ZIP);
            mailReply.reply(Constant.MESSAGE_FILE_MAIN_JAVA);
        }
    }

    private void handleAttachment(BodyPart part, String dir, String pathFileUnzip, String fileName) throws FileStorageException, IOException, MessagingException, InterruptedException {
        boolean stored = storeAttachments(part, dir, fileName);
        if (stored) {
            String pathUnzipped = unzipAttachments(dir, fileName, pathFileUnzip);
            if (!checkFile(pathUnzipped)) {
                LOGGER.error(Constant.MESSAGE_FILE_MAIN_JAVA);
                mailReply.reply(Constant.MESSAGE_FILE_MAIN_JAVA);
            }
            // TEST RUN CMD
            unitTest(pathFileUnzip, pathUnzipped);
        } else throw new FileStorageException("Can't storage attachment");
    }

    private void unitTest(String pathFileUnzip, String pathUnzipped) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("javac -cp src " + pathUnzipped);
        process.waitFor();
        Runtime.getRuntime().exec("java -cp " + pathFileUnzip + "/ Main");
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
    public void downloadEmailAttachments(String protocol, String hostname, String port, String username, String password, String subject, String expired) throws InterruptedException {
        LOGGER.debug(String.format("Info: '{'protocol: %s, hostname: %s, port: %s, username: %s, password: %s'}'", protocol, hostname, port, username, password));
        Properties properties = getServerProperties(protocol, hostname, port, username, password);
        Session session = Session.getDefaultInstance(properties);

        try {
            Store store = connect(session, protocol, hostname, username, password);
            Folder folderInbox = store.getFolder(Constant.FOLDER);
            folderInbox.open(Folder.READ_WRITE);

            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, Constant.FLAG_TERM);
            fetchNewMessages(folderInbox, unseenFlagTerm, username, subject, expired);

            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            LOGGER.error(String.format("No provider for protocol: %s", protocol));
        } catch (MessagingException ex) {
            LOGGER.error("Could not connect to the message store.");
        } catch (IOException e) {
            LOGGER.error("Load content failed.");
        } catch (FileStorageException e) {
            LOGGER.error(e.getMessage());
        }
    }


    /**
     * Check valid messages.
     *
     * @param message
     * @param subject
     * @param expired
     * @return
     * @throws MessagingException
     */
    private boolean isValidMessage(Message message, String subject, String expired) throws MessagingException {
        return message.getSubject().startsWith(subject)
                && message.getContentType().contains(Constant.CONTENT_TYPE_MULTIPART)
                && isExpiredMessage(message.getSentDate(), expired);
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
     * Store attachments
     *
     * @param part
     * @param dir
     * @param fileName
     * @throws IOException
     * @throws MessagingException
     */
    private boolean storeAttachments(BodyPart part, String dir, String fileName) throws IOException, MessagingException {
        File tmpDir = new File(dir);
        if (!tmpDir.exists()) {
            boolean isSuccess = tmpDir.mkdirs();
            LOGGER.info(String.format("Directory %%s created? %%s%s%s", tmpDir, isSuccess));
        }

        InputStream inputStream = part.getInputStream();
        File file = new File(String.format("%s%s%s", tmpDir.getPath(), File.separator, fileName));

        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] data = new byte[Constant.BYTE];
            int count;
            while ((count = inputStream.read(data)) > 0) {
                LOGGER.debug(count);
                outputStream.write(data, 0, count);
            }
            LOGGER.debug(String.format("File download: %s/%s", tmpDir, fileName));
            return new File(String.format("%s/%s", tmpDir.getPath(), fileName)).isFile();
        }
    }

    /**
     * Unzip file downloaded
     *
     * @param dir
     * @param fileName
     * @throws IOException
     */
    private String unzipAttachments(String dir, String fileName, String destDir) throws IOException {
        FileHandler fileHandler = new FileHandler();
        return fileHandler.unzip(String.format("%s/%s", dir, fileName), destDir);
    }

    private boolean checkFile(String dirUnzip) {
        try (Stream<Path> walk = Files.walk(Paths.get(dirUnzip))) {
            List<String> result = walk.map(Path::toString)
                    .filter(f -> f.contains("Main.java"))
                    .collect(Collectors.toList());
            if (result.isEmpty()) {
                return false;
            }
        } catch (IOException e) {
            LOGGER.error("Tap tin khong duoc nen tu file Main.java");
            return false;
        }
        return true;
    }


    /**
     * Get extension file
     *
     * @param fileName
     * @return
     */
    private Optional<String> getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return lastIndex == -1 ? Optional.empty() : Optional.of(fileName.substring(lastIndex + 1));
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
