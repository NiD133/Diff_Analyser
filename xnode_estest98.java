package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Tests that getBooleanAttribute correctly parses the string "false"
     * into a Boolean FALSE value, ignoring the provided default.
     */
    @Test
    public void getBooleanAttributeShouldReturnFalseWhenAttributeValueIsStringFalse() {
        // Arrange
        final String attributeName = "enabled";
        final Boolean defaultValue = true; // Use a non-false default to ensure it's ignored.

        // Create a DOM Node with an attribute set to the string "false".
        // IIOMetadataNode is a convenient, standard library implementation of org.w3c.dom.Node.
        Node node = new IIOMetadataNode();
        node.setAttribute(attributeName, "false");

        // Create an XNode instance. The XPathParser and Properties are required by the
        // constructor but are not relevant to this specific test.
        XPathParser parser = new XPathParser((Document) null, true);
        Properties variables = new Properties();
        XNode xNode = new XNode(parser, node, variables);

        // Act
        Boolean actualValue = xNode.getBooleanAttribute(attributeName, defaultValue);

        // Assert
        // The method should parse the attribute's string value "false" and return Boolean.FALSE.
        assertEquals(Boolean.FALSE, actualValue);
    }
}