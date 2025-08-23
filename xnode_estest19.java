package org.apache.ibatis.parsing;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getIntBody(Integer def) returns the provided default value (null)
     * when the XML node has no body content.
     */
    @Test
    public void getIntBodyShouldReturnNullDefaultWhenNodeBodyIsEmpty() {
        // Arrange
        // An empty DOM node will result in an XNode with a null body.
        Node emptyNode = new IIOMetadataNode();
        XNode xNode = new XNode(null, emptyNode, new Properties());
        Integer defaultValue = null;

        // Act
        // Call the method under test with a null default value.
        Integer result = xNode.getIntBody(defaultValue);

        // Assert
        // The result should be the provided default value.
        assertNull("Expected getIntBody to return the null default value for an empty node body.", result);
    }
}