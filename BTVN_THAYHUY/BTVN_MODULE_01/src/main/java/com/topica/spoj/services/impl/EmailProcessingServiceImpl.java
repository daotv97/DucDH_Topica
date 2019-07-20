package com.topica.spoj.services.impl;

import com.topica.spoj.exception.DataTransmissionException;
import com.topica.spoj.fetch.EmailDownloader;
import com.topica.spoj.services.EmailProcessingService;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;

@Named("emailProcessingServiceImpl")
public class EmailProcessingServiceImpl implements EmailProcessingService {
    private static final Logger LOGGER = Logger.getLogger(EmailProcessingServiceImpl.class.getName());

    @Inject
    @Named("imapProtocol")
    private EmailDownloader downloader;

    public void download(String username, String password) throws DataTransmissionException {
        if (username == null || password == null) {
            LOGGER.error(new StringBuilder()
                    .append("Username or password is null: ")
                    .append("{ username: ")
                    .append(username)
                    .append(", password: ")
                    .append(password)
                    .append(" }")
                    .toString());
            throw new DataTransmissionException("Username or password is null.");
        }
        downloader.download(username, password);
    }
}
