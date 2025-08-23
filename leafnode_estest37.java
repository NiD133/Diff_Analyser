package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the LeafNode class, focusing on attribute handling.
 */
public class LeafNodeTest {

    /**
     * Verifies that the attr(key, value) method returns the same node instance (this)
     * to allow for a fluent API, and that it does not unexpectedly alter the node's parent.
     */
    @Test
    public void settingAttributeReturnsSameNodeInstanceWithoutAlteringParent() {
        // Arrange: Create a CDataNode, which is a type of LeafNode.
        // A newly created node should have no parent.
        CDataNode cdataNode = new CDataNode("Initial CData text");
        assertNull("A new node should not have a parent.", cdataNode.parent());

        // Act: Set an attribute and capture the returned node.
        Node returnedNode = cdataNode.attr("data-id", "123");

        // Assert:
        // 1. The method should return the same instance for method chaining.
        assertSame("The attr() method should return the same node instance.", cdataNode, returnedNode);

        // 2. Setting an attribute should not affect the node's parentage.
        assertNull("Setting an attribute should not assign a parent to the node.", returnedNode.parent());

        // 3. Verify the attribute was set correctly as a sanity check.
        assertEquals("123", cdataNode.attr("data-id"));
    }
}