package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link LeafNode} abstract class.
 */
public class LeafNodeTest {

    /**
     * Verifies that a LeafNode, by definition, has a child node size of 0.
     * Leaf nodes (like DataNode, TextNode, Comment) cannot contain other nodes.
     */
    @Test
    public void childNodeSizeIsAlwaysZero() {
        // Arrange: Create a concrete instance of a LeafNode.
        // DataNode is used here as a representative example.
        LeafNode leafNode = new DataNode("Sample data");

        // Act: Get the number of child nodes.
        int childCount = leafNode.childNodeSize();

        // Assert: The size must be 0, as leaf nodes cannot have children.
        assertEquals("A leaf node must not have any children", 0, childCount);
    }
}