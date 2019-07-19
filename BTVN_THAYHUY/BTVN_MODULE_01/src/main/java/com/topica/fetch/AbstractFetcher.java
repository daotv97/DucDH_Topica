package com.topica.fetch;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public abstract class AbstractFetcher implements Fetcher {

    public void fetch(String storeType, String nameStore, String hostname, String username, String password) throws MessagingException {
        Properties properties = createProperties(storeType, hostname, username, password);
        Session emailSession = Session.getDefaultInstance(properties);
        Store store = storeAndConnectToServer(emailSession, nameStore, hostname, username, password);
    }

    private Properties createProperties(String storeType, String host, String username, String password) {
        Properties properties = new Properties();
        properties.put(Constant.STORE_PROPERTY, storeType);
        properties.put(Constant.HOST_PROPERTY, host);
        properties.put(Constant.USERNAME_PROPERTY, username);
        properties.put(Constant.PASSWORD_PROPERTY, password);
        return properties;
    }

    private Store storeAndConnectToServer(Session session, String nameStore, String hostname, String username, String password) throws MessagingException {
        Store store = session.getStore(nameStore);
        store.connect(session.getProperty(hostname)
                , session.getProperty(username)
                , session.getProperty(password));
        return store;
    }
}
