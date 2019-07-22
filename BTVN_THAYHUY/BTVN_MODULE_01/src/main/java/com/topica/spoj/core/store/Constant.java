package com.topica.spoj.core.store;

public class Constant {
    static final String SOCKET_CLASS_PROPERTY = "mail.%s.socketFactory.class";
    static final String SOCKET_FALLBACK_PROPERTY = "mail.%s.socketFactory.fallback";
    static final String SOCKET_FALLBACK_PORT = "mail.%s.socketFactory.port";
    static final String HOST_NAME_PROPERTY = "mail.%s.host";
    static final String PORT_PROPERTY = "mail.%s.port";
    static final String USERNAME_PROPERTY = "mail.%s.username";
    static final String PASS_PROPERTY = "mail.%s.password";
    static final String START_TLS_PROPERTY = "mail.%s.starttls.enable";

    static final String SOCKET_CLASS_VALUE = "javax.net.ssl.SSLSocketFactory";
    static final String SOCKET_FALLBACK_VALUE = "false";
    static final String START_TLS_VALUE = "true";
    static final String FOLDER = "INBOX";
    static final String CONTENT_TYPE_MULTIPART = "multipart";
    static final boolean FLAG_TERM = false;
    static final String ZIP = "zip";
    static final String UNZIP = "unzip";
    static final Integer BYTE = 1024;
    static final String MESSAGE_FILE_ZIP = "Yeu cau gui file dinh kem co duoi zip.";
    static final String MESSAGE_FILE_MAIN_JAVA = "Yeu cau gui file dinh kem co duoi zip.";


    private Constant() {

    }
}
