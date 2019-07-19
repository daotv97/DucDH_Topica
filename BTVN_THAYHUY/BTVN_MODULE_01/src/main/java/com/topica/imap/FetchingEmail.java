package com.topica.imap;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class FetchingEmail implements Fetcher {

    public void fetch() throws NoSuchFieldException, IOException, MessagingException {
        AtomicReference<Properties> defaultProperties = getDefaultProperties();
        Properties properties = defaultProperties.get();
        fetch(properties.getProperty(Constant.STORE_PROPERTY)
                , properties.getProperty(Constant.NAME_STORE)
                , properties.getProperty(Constant.USERNAME_PROPERTY)
                , properties.getProperty(Constant.USERNAME_PROPERTY)
                , properties.getProperty(Constant.PASSWORD_PROPERTY));
    }

    public void fetch(String storeType, String nameStore, String hostname, String username, String password) throws MessagingException {
        Properties properties = createProperties(storeType, hostname, username, password);
        Session emailSession = Session.getDefaultInstance(properties);

        Store store = emailSession.getStore(nameStore);
        store.connect(properties.getProperty(hostname)
                , properties.getProperty(username)
                , properties.getProperty(password));
    }

    private AtomicReference<Properties> getDefaultProperties() throws NoSuchFieldException, IOException {
        InputStream resource = FetchingEmail.class.getClassLoader().getResourceAsStream(Constant.PROPERTIES_FILE_NAME);
        if (resource == null) {
            throw new NoSuchFieldException("Sorry! unable to find...");
        }
        AtomicReference<Properties> properties = new AtomicReference<Properties>(new Properties());
        properties.get().load(resource);
        return properties;
    }

    private Properties createProperties(String storeType, String host, String username, String password) {
        Properties properties = new Properties();
        properties.put(Constant.STORE_PROPERTY, storeType);
        properties.put(Constant.HOST_PROPERTY, host);
        properties.put(Constant.USERNAME_PROPERTY, username);
        properties.put(Constant.PASSWORD_PROPERTY, password);
        return properties;
    }
}
