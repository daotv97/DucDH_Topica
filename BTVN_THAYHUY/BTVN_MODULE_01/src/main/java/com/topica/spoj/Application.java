package com.topica.spoj;

import com.topica.spoj.exception.DataTransmissionException;
import com.topica.spoj.services.impl.EmailProcessingServiceImpl;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

public class Application {

    public static void main(String[] args) throws DataTransmissionException {
        String userName = "ittalent@topica.edu.vn";
        String password = "topica123";
        Weld weld = new Weld();
        WeldContainer container = weld.initialize();
        BeanManager bm = container.getBeanManager();
        Bean<EmailProcessingServiceImpl> bean = (Bean<EmailProcessingServiceImpl>) bm.getBeans("emailProcessingServiceImpl").iterator().next();
        CreationalContext<EmailProcessingServiceImpl> ctx = bm.createCreationalContext(bean);
        EmailProcessingServiceImpl jdbc = (EmailProcessingServiceImpl) bm.getReference(bean, EmailProcessingServiceImpl.class, ctx);
        jdbc.download(userName, password);
    }
}
