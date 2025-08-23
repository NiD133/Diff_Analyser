package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getFloatBody(defaultValue) returns the provided default value
     * when the XNode has a null or empty body.
     */
    @Test
    public void shouldReturnDefaultFloatWhenBodyIsEmpty() {
        // Arrange
        // Create an empty DOM Node, which results in a null body when parsed by XNode.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());
        Float defaultValue = 123.45f;

        // Act
        Float actualValue = xNode.getFloatBody(defaultValue);

        // Assert
        // The method should fall back to the default value.
        assertEquals(defaultValue, actualValue);
    }
}