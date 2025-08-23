package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    /**
     * Tests that getFloatAttribute returns the provided default value
     * when the requested attribute does not exist on the node.
     */
    @Test
    public void getFloatAttributeShouldReturnDefaultValueWhenAttributeIsMissing() {
        // Arrange
        // 1. Create a DOM node that has no attributes.
        Node nodeWithoutAttributes = new IIOMetadataNode();
        XNode xNode = new XNode(null, nodeWithoutAttributes, new Properties());

        // 2. Define the attribute to look for and the default value to return.
        String nonExistentAttributeName = "port";
        Float defaultValue = 8080.5f;

        // Act
        // 3. Attempt to get the float value of the non-existent attribute.
        Float actualValue = xNode.getFloatAttribute(nonExistentAttributeName, defaultValue);

        // Assert
        // 4. Verify that the method returned the default value.
        assertEquals(defaultValue, actualValue);
    }
}