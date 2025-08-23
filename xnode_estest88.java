package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the XNode class.
 * This refactored test focuses on a specific behavior of the getStringAttribute method.
 */
public class XNodeTest {

    /**
     * Verifies that getStringAttribute() returns null when the requested attribute does not exist.
     */
    @Test
    public void getStringAttributeShouldReturnNullForNonExistentAttribute() {
        // Arrange
        // Create an empty DOM node that has no attributes.
        // IIOMetadataNode is a standard, concrete implementation of the org.w3c.dom.Node interface.
        Node nodeWithoutAttributes = new IIOMetadataNode();
        Properties variables = new Properties();

        // The XPathParser is not used by the getStringAttribute method, so it can be null for this test.
        XNode xNode = new XNode(null, nodeWithoutAttributes, variables);

        // Act
        String attributeValue = xNode.getStringAttribute("nonExistentAttribute");

        // Assert
        assertNull("Expected a null value for an attribute that does not exist.", attributeValue);
    }
}