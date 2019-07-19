package com.topica.spoj.fetch;


import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public abstract class AbstractFetcher implements Fetcher {

    public void store(ConnectProperties connectProperties) throws MessagingException {
        Properties properties = createProperties(storeType, hostname, username, password);
        Session emailSession = Session.getDefaultInstance(properties);
        Store store = storeAndConnectToServer(emailSession, nameStore, hostname, username, password);
    }

    private Store storeAndConnectToServer(Session session, String nameStore, String hostname, String username, String password) throws MessagingException {
        Store store = session.getStore(nameStore);
        store.connect(hostname, username, password);
        return store;
    }

    private Properties createProperties(String storeType, String host, String port, String username, String password) {
        Properties properties = new Properties();
        properties.put(Constant.STORE_PROPERTY, storeType);
        properties.put(Constant.HOST_PROPERTY, host);
        properties.put(Constant.PORT_PROPERTY, port);
        properties.put(Constant.USERNAME_PROPERTY, username);
        properties.put(Constant.PASS_PROPERTY, password);
        return properties;
    }
}
