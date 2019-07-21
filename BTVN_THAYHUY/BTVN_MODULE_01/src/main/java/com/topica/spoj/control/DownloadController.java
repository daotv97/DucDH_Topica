package com.topica.spoj.control;

import com.topica.spoj.core.exception.DataTransmissionException;
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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.IntStream;

public class DownloadController {
    private static final Logger LOGGER = Logger.getLogger(DownloadController.class.getName());
    private static final String PATH_FILE_SYSTEM = "src/main/resources/system-info/system.xml";
    private static final String SYSTEM_INFO = "homework";
    private static final String ELEMENT_ATTRIBUTE_NAME = "name";
    private static final String ELEMENT_TAG_USERNAME = "username";
    private static final String ELEMENT_TAG_PASS = "password";
    private static final String ELEMENT_TAG_SUBJECT = "subject";
    private static final String ELEMENT_TAG_EXPIRED = "expired";

    private static Element readDataFromXml(String nameHomework) throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File(PATH_FILE_SYSTEM);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName(SYSTEM_INFO);

        return IntStream.range(0, nodeList.getLength())
                .mapToObj(nodeList::item)
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .map(node -> (Element) node)
                .filter(element -> element.getAttribute(ELEMENT_ATTRIBUTE_NAME).equals(nameHomework))
                .findFirst().orElse(null);
    }

    private static String getDataElement(String name, Element element) {
        return element.getElementsByTagName(name).item(0).getTextContent();
    }

    public void downloadAndStoreAttachments() {
        Weld weld = new Weld();
        WeldContainer container = weld.initialize();
        BeanManager beanManager = container.getBeanManager();
        Bean<?> bean = beanManager.getBeans("emailProcessingServiceImpl").iterator().next();
        CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
        EmailProcessingServiceImpl jdbc = (EmailProcessingServiceImpl) beanManager.getReference(bean, EmailProcessingServiceImpl.class, ctx);

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
}
