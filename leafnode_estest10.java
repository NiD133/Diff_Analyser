package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link LeafNode#empty()} method.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling empty() on a LeafNode, which cannot have children,
     * is a no-op and returns the node instance itself.
     */
    @Test
    public void emptyOnLeafNodeShouldReturnSameInstance() {
        // Arrange: Create an instance of XmlDeclaration, a concrete subclass of LeafNode.
        LeafNode leafNode = new XmlDeclaration("xml", false);

        // Act: Call the empty() method.
        Node result = leafNode.empty();

        // Assert: The returned node should be the exact same instance as the original.
        assertSame("Calling empty() on a LeafNode should return the instance itself.", leafNode, result);
    }
}