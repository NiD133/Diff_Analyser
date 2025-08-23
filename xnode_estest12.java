package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getLongBody() returns the provided default value
     * when the node's body is empty.
     */
    @Test
    public void getLongBodyShouldReturnDefaultValueWhenBodyIsEmpty() {
        // Arrange
        // An empty IIOMetadataNode serves as a mock Node with no text content.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());
        Long defaultValue = 0L;

        // Act
        // Attempt to get the body as a Long, providing a default value.
        Long result = xNode.getLongBody(defaultValue);

        // Assert
        // The result should be the default value, as the node's body is empty and cannot be parsed.
        assertEquals(defaultValue, result);
    }
}