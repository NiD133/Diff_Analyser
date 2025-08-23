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
     * Verifies that getIntBody(defaultValue) returns the provided default value
     * when the underlying XML node has no text content (i.e., an empty body).
     */
    @Test
    public void getIntBodyShouldReturnDefaultValueWhenBodyIsEmpty() {
        // Arrange: Create an XNode that wraps an empty DOM node.
        // An empty node results in a null body, which cannot be parsed as an integer.
        Node emptyNode = new IIOMetadataNode();
        XNode xNodeWithEmptyBody = new XNode(null, emptyNode, new Properties());
        Integer defaultValue = -1150;

        // Act: Call the method under test with the default value.
        Integer actualValue = xNodeWithEmptyBody.getIntBody(defaultValue);

        // Assert: The result should be the default value we provided.
        assertEquals(defaultValue, actualValue);
    }
}