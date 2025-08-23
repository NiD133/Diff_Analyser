package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the XNode class, focusing on attribute retrieval methods.
 */
public class XNodeGetAttributeTest {

    /**
     * Verifies that getLongAttribute() returns the provided default value
     * when the requested attribute does not exist on the XML node.
     */
    @Test
    public void getLongAttribute_shouldReturnDefaultValue_whenAttributeIsMissing() {
        // Arrange
        // Create an XML node that has no attributes.
        Node nodeWithoutAttributes = new IIOMetadataNode();
        XNode xNode = new XNode(null, nodeWithoutAttributes, new Properties());

        String nonExistentAttributeName = "timeout";
        Long defaultValue = 5000L;

        // Act
        Long actualValue = xNode.getLongAttribute(nonExistentAttributeName, defaultValue);

        // Assert
        assertEquals(defaultValue, actualValue);
    }
}