package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link LeafNode} class, focusing on attribute-related behavior
 * using a concrete implementation like {@link CDataNode}.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling attr() on a CDataNode with its node name ("#cdata") as the key
     * correctly retrieves the node's text content. This is a special-case behavior
     * where the node's content is treated like an attribute.
     */
    @Test
    public void attrOnCDataNodeWithNodeNameKeyReturnsContent() {
        // Arrange: Create a CDataNode with specific text content.
        String expectedContent = "A piece of content";
        CDataNode cdataNode = new CDataNode(expectedContent);

        // The key to retrieve the content is the node's own name.
        String contentAccessKey = "#cdata";
        assertEquals(contentAccessKey, cdataNode.nodeName()); // Self-verify the key is correct

        // Act: Call the attr() method using the node's name as the key.
        String actualContent = cdataNode.attr(contentAccessKey);

        // Assert: The returned value should be the node's content, not a regular attribute.
        assertEquals(expectedContent, actualContent);
    }
}