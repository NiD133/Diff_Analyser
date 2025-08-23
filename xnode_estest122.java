package org.apache.ibatis.parsing;

import static org.junit.Assert.assertNull;

import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Contains tests for the {@link XNode} class, focusing on body parsing methods.
 */
public class XNodeTest {

    /**
     * Verifies that getLongBody() returns null when the underlying XML node
     * has no text content or body.
     */
    @Test
    public void shouldReturnNullForLongBodyWhenNodeHasNoContent() {
        // Arrange
        // 1. Create an empty DOM node. IIOMetadataNode is a convenient, standard implementation.
        Node emptyNode = new IIOMetadataNode();

        // 2. Instantiate an XNode wrapping the empty node.
        // The XPathParser and variables are required for the constructor but are not
        // directly relevant to this specific test's logic.
        XPathParser xPathParser = new XPathParser((Document) null, false);
        Properties variables = new Properties();
        XNode xNode = new XNode(xPathParser, emptyNode, variables);

        // Act
        // Attempt to parse the node's body as a Long.
        Long result = xNode.getLongBody();

        // Assert
        // The result should be null, as there is no body content to parse.
        assertNull(result);
    }
}