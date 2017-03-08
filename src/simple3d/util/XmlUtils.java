package simple3d.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfisher on 07/03/2017.
 */
public class XmlUtils {
    public static Element getRoot(String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        File inputFile = new File(xmlPath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        Element rootElement = doc.getDocumentElement();
        rootElement.normalize();

        return rootElement;
    }

    public static Element getFirstChild(Element element, String tagName) {
        NodeList childNodes = element.getChildNodes();

        for(int i=0; i< childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if(childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals(tagName)) {
                return (Element) childNode;
            }
        }

        return null;
    }

    public static Element[] getChildren(Element element, String tagName) {
        NodeList childNodes = element.getChildNodes();

        List<Element> childElements = new ArrayList<Element>();
        for(int i=0; i< childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if(childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals(tagName)) {
                Element childElement = (Element) childNode;
                childElements.add(childElement);
            }
        }

        return childElements.toArray(new Element[childElements.size()]);
    }
}

