package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the abstract LeafNode class, focusing on behavior common to all its subclasses.
 */
public class LeafNodeTest {

    /**
     * Verifies that a newly created LeafNode, which has no attributes by default,
     * correctly reports that it has no attributes.
     */
    @Test
    public void hasAttributesReturnsFalseWhenNodeHasNoAttributes() {
        // Arrange: A LeafNode is abstract, so we must use a concrete subclass for testing.
        // CDataNode is a simple LeafNode implementation suitable for this purpose.
        LeafNode node = new CDataNode("");

        // Act & Assert: Check if the node reports having attributes.
        // It should return false as no attributes have been added.
        assertFalse(node.hasAttributes());
    }
}