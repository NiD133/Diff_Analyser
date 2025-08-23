package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the LeafNode abstract class.
 */
public class LeafNodeTest {

    /**
     * Verifies that setting an attribute on a LeafNode does not change its
     * fundamental characteristic of having no children.
     */
    @Test
    public void settingAttributeOnLeafNodeDoesNotAddChildren() {
        // Arrange: Create a CDataNode, which is a concrete implementation of LeafNode.
        CDataNode cdataNode = new CDataNode("Initial content");

        // Act: Set an attribute on the node.
        cdataNode.attr("test-key", "test-value");

        // Assert: The node should remain a leaf node with zero children.
        assertEquals(0, cdataNode.childNodeSize());
    }
}