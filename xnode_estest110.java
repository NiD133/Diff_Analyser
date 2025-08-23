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
     * Verifies that getFloatBody() returns null when the underlying XML node
     * has no text content, resulting in an empty body.
     */
    @Test
    public void shouldReturnNullFloatBodyWhenNodeBodyIsEmpty() {
        // Arrange
        // An IIOMetadataNode is a concrete implementation of org.w3c.dom.Node.
        // A default instance has no text content, leading to a null body in the XNode.
        Node nodeWithEmptyBody = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(null, nodeWithEmptyBody, variables);

        // Act
        Float bodyAsFloat = xNode.getFloatBody();

        // Assert
        assertNull("getFloatBody() should return null for an empty node body.", bodyAsFloat);
    }
}