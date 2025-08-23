package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the LeafNode class.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling the empty() method on a LeafNode does not remove it from its parent.
     * Since a LeafNode cannot have children, empty() should be a no-op that returns the node itself
     * without affecting its parent relationship.
     */
    @Test
    public void emptyMethodOnLeafNodeShouldNotAffectParent() {
        // Arrange: Create a LeafNode (a CDataNode in this case) and add it to a parent Element.
        Element parent = new Element("div");
        CDataNode leafNode = new CDataNode("some data");
        parent.appendChild(leafNode);

        // Sanity check to ensure the node has a parent before the action.
        assertTrue("Pre-condition failed: leafNode should have a parent.", leafNode.hasParent());

        // Act: Call the empty() method on the leaf node.
        Node result = leafNode.empty();

        // Assert: The node should still have its parent, and the method should return the same instance.
        assertSame("empty() should return the same node instance.", leafNode, result);
        assertTrue("A LeafNode should still have a parent after empty() is called.", result.hasParent());
        assertSame("The parent of the LeafNode should remain unchanged.", parent, result.parent());
    }
}