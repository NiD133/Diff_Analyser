package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;
import static org.junit.Assert.assertNull;

/**
 * Test suite for the XNode class.
 */
public class XNodeTest {

    @Test
    public void getFloatAttributeShouldReturnNullWhenAttributeDoesNotExist() {
        // Arrange
        // Create a DOM node that has no attributes.
        // IIOMetadataNode is a concrete implementation of org.w3c.dom.Node,
        // suitable for creating a simple, attribute-less node for this test.
        IIOMetadataNode nodeWithoutAttributes = new IIOMetadataNode();
        XNode xNode = new XNode(null, nodeWithoutAttributes, new Properties());

        // Act
        // Attempt to retrieve a float value from an attribute that does not exist.
        Float attributeValue = xNode.getFloatAttribute("nonExistentAttribute");

        // Assert
        assertNull("The method should return null when the float attribute is not found.", attributeValue);
    }
}