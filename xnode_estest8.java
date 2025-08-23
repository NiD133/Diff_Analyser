package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Properties;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Tests that getStringAttribute returns the provided default value (null)
     * when the requested attribute does not exist on the XML node.
     */
    @Test
    public void shouldReturnDefaultNullWhenAttributeIsMissing() throws Exception {
        // Arrange
        // 1. Create a standard DOM Node from a simple XML string.
        //    This <user/> node has no attributes.
        String xml = "<user/>";
        Node domNode = buildNodeFromXML(xml);

        // 2. The XNode requires an XPathParser and variables for its constructor.
        //    For this specific test, they can be simple, default instances.
        XPathParser xPathParser = new XPathParser(xml);
        Properties variables = new Properties();
        XNode xNode = new XNode(xPathParser, domNode, variables);

        String nonExistentAttributeName = "id";
        String defaultValue = null;

        // Act
        String actualValue = xNode.getStringAttribute(nonExistentAttributeName, defaultValue);

        // Assert
        assertNull("getStringAttribute should return the default value (null) when the attribute does not exist.", actualValue);
    }

    /**
     * Helper method to create a W3C DOM Node from an XML string.
     * @param xml The XML string to parse.
     * @return The root Node of the parsed document.
     * @throws Exception if parsing fails.
     */
    private Node buildNodeFromXML(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        return document.getDocumentElement();
    }
}