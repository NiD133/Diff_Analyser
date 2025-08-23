package org.apache.ibatis.parsing;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the XNode class, focusing on body parsing methods.
 */
public class XNodeTest {

    /**
     * Verifies that getIntBody(defaultValue) returns the provided default value
     * when the XML node's body is empty.
     */
    @Test
    public void getIntBodyShouldReturnDefaultWhenBodyIsEmpty() {
        // Arrange: Create an XNode with an empty body.
        // We use IIOMetadataNode as a standard, empty implementation of org.w3c.dom.Node.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());

        Integer defaultValue = 97;

        // Act: Call the method under test.
        Integer result = xNode.getIntBody(defaultValue);

        // Assert: The result should be the default value.
        assertEquals(defaultValue, result);
    }
}