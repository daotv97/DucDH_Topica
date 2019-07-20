package com.topica.spoj.fetch;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public abstract class AbstractFetcher implements Fetcher {

    private Store store = null;

    public void fetch(String storeType, String hostname, String port, String username, String password) throws MessagingException {
        Properties properties = createProperties(storeType, hostname, port, username, password);
        Session emailSession = Session.getDefaultInstance(properties);

        this.store = storeAndConnectToServer(emailSession, hostname, username, password);

        if(store != null) {
            createAndOpenFolderEmail();
        }
    }

    private Store storeAndConnectToServer(Session session, String hostname, String username, String password) throws MessagingException {
        this.store = session.getStore(Constant.STORE_NAME);
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

    private void createAndOpenFolderEmail() throws MessagingException {
        Folder emailFolder = this.store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);
    }
}
