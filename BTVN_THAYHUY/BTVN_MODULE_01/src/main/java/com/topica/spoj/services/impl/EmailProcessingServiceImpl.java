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

    public void download(String username, String password, String subject, String expired) throws DataTransmissionException {
        if (username == null || password == null || subject == null || expired == null) {
            LOGGER.error(String.format("Data is not allowed to be null : { username: %s, password: %s, subject: %s, expired: %s }", username, password, subject, expired));
            throw new DataTransmissionException("Data is not allowed to be null.");
        }
        LOGGER.debug(String.format("{username: %s, password: %s, subject: %s, expired: %s}", username, password, subject, expired));
        downloader.download(username, password, subject, expired);
    }
}
