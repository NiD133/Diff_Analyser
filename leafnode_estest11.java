package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the LeafNode abstract class.
 * This class focuses on behaviors specific to nodes that cannot have children.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling empty() on a LeafNode is a no-op and returns the same instance.
     */
    @Test
    public void emptyOnLeafNodeShouldReturnSameInstance() {
        // Background: A LeafNode, by definition, cannot have children. The empty()
        // method is designed to remove all children from a node.
        // Expectation: When called on a LeafNode, empty() should do nothing
        // and return the node itself to allow for method chaining.

        // Arrange: Create an instance of a LeafNode subclass. TextNode is used here.
        TextNode leafNode = TextNode.createFromEncoded("sample text");

        // Act: Call the empty() method.
        Node result = leafNode.empty();

        // Assert: The method should return the very same instance.
        assertSame("Calling empty() on a LeafNode should return the instance itself.", leafNode, result);
    }
}