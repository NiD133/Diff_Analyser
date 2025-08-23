package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Properties;

import static org.junit.Assert.assertFalse;

/**
 * Test suite for the XNode class.
 * This class focuses on attribute parsing functionality.
 */
public class XNodeTest {

    /**
     * Verifies that getBooleanAttribute returns false for any string value
     * that is not "true" (case-insensitive).
     *
     * This aligns with the behavior of Java's Boolean.valueOf(String).
     */
    @Test
    public void getBooleanAttributeShouldReturnFalseForNonTrueStringValue() throws Exception {
        // Arrange
        // 1. Create a valid XML Document and an Element to test with.
        //    This is more robust and readable than using IIOMetadataNode or null documents.
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element element = doc.createElement("config");

        String attributeName = "enabled";
        String attributeValue = "yes"; // A representative non-"true" string
        element.setAttribute(attributeName, attributeValue);

        // 2. Create the XNode instance to be tested.
        //    An XPathParser and Properties are required by the constructor.
        XPathParser parser = new XPathParser(doc);
        Properties variables = new Properties();
        XNode xNode = new XNode(parser, element, variables);

        // Act
        Boolean result = xNode.getBooleanAttribute(attributeName);

        // Assert
        assertFalse("getBooleanAttribute should return false for a non-'true' string value like 'yes'.", result);
    }
}