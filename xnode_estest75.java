package org.apache.ibatis.parsing;

import org.junit.Test;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;
import java.util.Properties;
import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getIntAttribute returns the provided default value (null)
     * when the requested attribute does not exist.
     */
    @Test
    public void getIntAttributeShouldReturnNullDefaultWhenAttributeIsMissing() {
        // Arrange
        // Create an XNode based on a DOM node that has no attributes.
        Node nodeWithoutAttributes = new IIOMetadataNode();
        XNode xNode = new XNode(null, nodeWithoutAttributes, new Properties());
        
        String nonExistentAttributeName = "count";
        Integer defaultValue = null;

        // Act
        // Attempt to get an integer attribute that is not present.
        Integer actualValue = xNode.getIntAttribute(nonExistentAttributeName, defaultValue);

        // Assert
        // The method should return the default value provided.
        assertNull("Expected a null value for a missing attribute when the default is null.", actualValue);
    }
}