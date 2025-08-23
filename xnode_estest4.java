package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that getStringBody(defaultValue) returns the provided default value
     * when the underlying XML node has no body content.
     */
    @Test
    public void getStringBodyShouldReturnDefaultValueWhenBodyIsEmpty() {
        // Arrange
        String defaultValue = "default body content";
        Node emptyNode = new IIOMetadataNode(); // An empty node with no text content.
        Properties variables = new Properties();

        // The XPathParser is not used by the getStringBody method, so it can be null for this test.
        XNode xNode = new XNode(null, emptyNode, variables);

        // Act
        String actualBody = xNode.getStringBody(defaultValue);

        // Assert
        assertEquals(defaultValue, actualBody);
    }
}