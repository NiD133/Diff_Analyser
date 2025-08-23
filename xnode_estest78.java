package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getStringAttribute(name, defaultValue) returns the actual
     * attribute value when the attribute exists, ignoring the default value.
     */
    @Test
    public void shouldReturnAttributeValueWhenAttributeExists() {
        // Arrange
        final String attributeName = "id";
        final String expectedValue = "test-node-123";
        final String defaultValue = "default-id";

        // Create a DOM node and set the attribute we want to test
        Node domNode = new IIOMetadataNode();
        domNode.getAttributes().setNamedItem(
            domNode.getOwnerDocument().createAttribute(attributeName)
        );
        domNode.getAttributes().getNamedItem(attributeName).setNodeValue(expectedValue);

        // Dependencies needed for XNode instantiation
        XPathParser xPathParser = new XPathParser((Document) null, false);
        Properties variables = new Properties();
        XNode xNode = new XNode(xPathParser, domNode, variables);

        // Act
        String actualValue = xNode.getStringAttribute(attributeName, defaultValue);

        // Assert
        assertEquals("The method should return the actual attribute value, not the default.",
            expectedValue, actualValue);
    }
}