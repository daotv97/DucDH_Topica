package com.topica.spoj.fetch;

class Constant {
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

    private Constant() {

    }
}
