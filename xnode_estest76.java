package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Verifies that getBooleanAttribute() returns the provided default value
     * when the requested attribute does not exist on the node.
     */
    @Test
    public void getBooleanAttributeShouldReturnDefaultValueWhenAttributeIsMissing() {
        // Arrange
        // Create a DOM node that has no attributes.
        Node nodeWithoutAttributes = new IIOMetadataNode();
        XNode xNode = new XNode(null, nodeWithoutAttributes, new Properties());

        String nonExistentAttributeName = "someMissingAttribute";
        Boolean defaultValue = true;

        // Act
        Boolean result = xNode.getBooleanAttribute(nonExistentAttributeName, defaultValue);

        // Assert
        // The method should return the default value because the attribute is not present.
        assertEquals(defaultValue, result);
    }
}