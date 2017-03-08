package simple3d.util;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by tfisher on 07/03/2017.
 */
class XmlUtilsTest {
    @Test
    void getRoot() {
        try {
            Element rootElement = XmlUtils.getRoot("test/resources/test.xml");

            assertEquals("root", rootElement.getNodeName());
        } catch(Exception e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    void getFirstChild() {
        try {
            Element rootElement = XmlUtils.getRoot("test/resources/test.xml");
            Element childElement = XmlUtils.getFirstChild(rootElement, "vertex");

            assertEquals("vertex", childElement.getNodeName());
            assertEquals("1", childElement.getAttribute("index"));

            //when the child is not found return null
            assertEquals(null, XmlUtils.getFirstChild(rootElement, "nonexistent"));
        } catch(Exception e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    void getChildren() {
        try {
            Element rootElement = XmlUtils.getRoot("test/resources/test.xml");
            Element[] childElements = XmlUtils.getChildren(rootElement, "vertex");

            assertEquals(2, childElements.length);
            assertEquals("vertex", childElements[0].getNodeName());
            assertEquals("1", childElements[0].getAttribute("index"));
            assertEquals("vertex", childElements[1].getNodeName());
            assertEquals("2", childElements[1].getAttribute("index"));

            //when children are not found, return empty Element array
            org.w3c.dom.Element[] noChildElements = XmlUtils.getChildren(rootElement, "nonexistent");
            assertEquals(0, noChildElements.length);
        } catch(Exception e) {
            fail("Should not have thrown exception");
        }
    }

}