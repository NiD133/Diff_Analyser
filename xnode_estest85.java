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
     * Verifies that getStringBody(String) returns the provided null default value
     * when the underlying XML node has no body content.
     */
    @Test
    public void getStringBodyShouldReturnNullDefaultWhenNodeIsEmpty() {
        // Arrange: Create an XNode from an empty XML node.
        // An IIOMetadataNode is a simple, standard way to create a blank DOM Node for testing.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());
        String nullDefaultValue = null;

        // Act: Call the method under test with a null default.
        String actualBody = xNode.getStringBody(nullDefaultValue);

        // Assert: The result should be the null default value.
        assertNull("Expected a null body when the node is empty and the default is null.", actualBody);
    }
}