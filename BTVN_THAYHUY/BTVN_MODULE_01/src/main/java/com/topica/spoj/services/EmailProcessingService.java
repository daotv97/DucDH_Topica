package com.topica.spoj.services;

import com.topica.spoj.exception.DataTransmissionException;

public interface EmailProcessingService {
    void download(String username, String password) throws DataTransmissionException;
}
