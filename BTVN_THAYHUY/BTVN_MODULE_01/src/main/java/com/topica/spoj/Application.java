package com.topica.spoj;

import com.topica.spoj.exception.DataTransmissionException;
import com.topica.spoj.services.impl.EmailProcessingServiceImpl;
import org.apache.log4j.Logger;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Application {
    private static final Logger LOGGER = Logger.getLogger(Application.class);
    private static final String PATH_FILE_SYSTEM = "src/main/resources/system-info/system.xml";
    private static final String SYSTEM_INFO = "system";
    private static final String ELEMENT_ATTRIBUTE_NAME = "name";
    private static final String ELEMENT_TAG_USERNAME = "username";
    private static final String ELEMENT_TAG_PASS = "password";
    private static final String ELEMENT_TAG_SUBJECT = "subject";
    private static final String ELEMENT_TAG_EXPIRED = "expired";

    public static void main(String[] args) {
        Weld weld = new Weld();
        WeldContainer container = weld.initialize();
        BeanManager bm = container.getBeanManager();
        Bean<EmailProcessingServiceImpl> bean = (Bean<EmailProcessingServiceImpl>) bm.getBeans("emailProcessingServiceImpl").iterator().next();
        CreationalContext<EmailProcessingServiceImpl> ctx = bm.createCreationalContext(bean);
        EmailProcessingServiceImpl jdbc = (EmailProcessingServiceImpl) bm.getReference(bean, EmailProcessingServiceImpl.class, ctx);
        try {
            LOGGER.info("Find homework by name: ");
            Element element = readDataFromXml(new Scanner(System.in).nextLine());
            if (element != null) {
                String username = getDataElement(ELEMENT_TAG_USERNAME, element);
                String password = getDataElement(ELEMENT_TAG_PASS, element);
                String subject = getDataElement(ELEMENT_TAG_SUBJECT, element);
                String expired = getDataElement(ELEMENT_TAG_EXPIRED, element);
                jdbc.download(username, password, subject, expired);
            } else LOGGER.info("Not found! Try again.");
        } catch (DataTransmissionException e) {
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Can't load file system.xml: " + e.getMessage());
        }
    }

    private static Element readDataFromXml(String nameHomework) throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File(PATH_FILE_SYSTEM);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document doc = documentBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName(SYSTEM_INFO);
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node nNode = nodeList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                if (element.getAttribute(ELEMENT_ATTRIBUTE_NAME).equals(nameHomework)) return element;
            }
        }
        return null;
    }

    private static String getDataElement(String name, Element element) {
        return element.getElementsByTagName(name).item(0).getTextContent();
    }
}
