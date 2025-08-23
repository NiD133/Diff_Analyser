package org.apache.ibatis.parsing;

import static org.junit.Assert.assertNull;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getBooleanBody() returns null when the underlying XML node
     * has no text content (i.e., an empty body).
     */
    @Test
    public void getBooleanBody_shouldReturnNull_whenNodeHasNoBodyContent() {
        // Arrange: Create an XNode based on a DOM node that has no text content.
        // IIOMetadataNode is a simple, concrete implementation of org.w3c.dom.Node.
        Node emptyNode = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(null, emptyNode, variables);

        // Act: Call the method under test.
        Boolean result = xNode.getBooleanBody();

        // Assert: The result should be null, as there is no body to parse.
        assertNull("getBooleanBody() should return null for a node with no body.", result);
    }
}