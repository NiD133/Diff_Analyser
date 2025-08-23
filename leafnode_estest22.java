package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the LeafNode abstract class.
 * This focuses on behavior common to all leaf nodes (e.g., TextNode, DataNode).
 */
public class LeafNodeTest {

    /**
     * Verifies that the attr(key, value) method returns the same node instance.
     * This is crucial for enabling a fluent API (method chaining).
     */
    @Test
    public void settingAttributeReturnsSameNodeInstanceForChaining() {
        // Arrange: Create a concrete LeafNode instance. A DataNode is a good example.
        // The initial content is not relevant to this test.
        LeafNode leafNode = new DataNode("some data");

        // Act: Set an attribute and capture the returned Node.
        Node returnedNode = leafNode.attr("id", "test-id");

        // Assert: The method should return the same instance it was called on.
        assertSame("The attr() method must return 'this' to support method chaining.", leafNode, returnedNode);

        // Also, verify the attribute was set correctly as a sanity check.
        assertEquals("test-id", leafNode.attr("id"));
    }
}