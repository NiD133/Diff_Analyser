package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Verifies that getFloatAttribute returns the specified default value
     * when the requested attribute does not exist on the node.
     */
    @Test
    public void getFloatAttributeShouldReturnDefaultValueWhenAttributeIsMissing() {
        // Arrange
        // Create a simple DOM node without any attributes.
        // IIOMetadataNode is a concrete implementation of org.w3c.dom.Node.
        Node domNode = new IIOMetadataNode();
        
        // The XPathParser is not used by the get*Attribute methods, so it can be null for this test.
        XNode xNode = new XNode(null, domNode, new Properties());

        String nonExistentAttribute = "height";
        Float defaultValue = null;

        // Act
        Float actualValue = xNode.getFloatAttribute(nonExistentAttribute, defaultValue);

        // Assert
        // The method should return the provided default value because the attribute is not found.
        assertNull("Expected default value (null) for a missing attribute", actualValue);
    }
}