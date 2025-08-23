package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the LeafNode class.
 */
public class LeafNodeTest {

    /**
     * Verifies that adding an attribute to a LeafNode (specifically a CDataNode)
     * does not add any child nodes, confirming it remains a leaf.
     */
    @Test
    public void addingAttributeToLeafNodeShouldNotAddChildren() {
        // Arrange: Create a CDataNode, which is a type of LeafNode.
        CDataNode cdataNode = new CDataNode("Initial content");

        // Act: Add an attribute to the node. The attr method should return the same node instance.
        Node resultNode = cdataNode.attr("newAttribute", "newValue");

        // Assert: Verify that the node is still a leaf node with zero children
        // and that the method returned the same instance.
        assertEquals("The node should have 0 children after adding an attribute.", 0, resultNode.childNodeSize());
        assertSame("The attr method should return the same node instance.", cdataNode, resultNode);
    }
}