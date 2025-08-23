package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the LeafNode abstract class, focusing on its core invariants.
 */
public class LeafNodeTest {

    /**
     * Verifies that manipulating attributes on a LeafNode does not alter its
     * fundamental property of having zero children.
     */
    @Test
    public void attributeModificationShouldNotAddChildrenToLeafNode() {
        // Arrange: Create a DataNode, which is a concrete implementation of LeafNode.
        DataNode dataNode = new DataNode("some data");
        assertEquals("A new LeafNode should have zero children", 0, dataNode.childNodeSize());

        // Act: Perform an attribute operation. Calling attr() with a null value
        // is equivalent to removing the attribute. This tests that such an
        // operation doesn't have unintended side effects.
        Node returnedNode = dataNode.attr("id", null);

        // Assert:
        // 1. The primary assertion: A LeafNode must always have zero children,
        //    even after its attributes are modified.
        assertEquals("A LeafNode must never have children after attribute modification", 0, dataNode.childNodeSize());

        // 2. The method should return the same instance to allow for method chaining.
        assertSame("The method should return the same node for chaining", dataNode, returnedNode);
    }
}