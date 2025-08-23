package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the abstract LeafNode class, focusing on cloning behavior.
 */
public class LeafNodeTest {

    /**
     * Verifies that the doClone() method correctly assigns the specified parent to the new clone.
     * This is important to ensure that cloned nodes are correctly integrated into a new
     * document structure.
     */
    @Test
    public void cloneShouldHaveParentWhenParentIsProvided() {
        // Arrange: Create an original node and a new parent for its clone.
        CDataNode originalNode = new CDataNode("some-cdata");
        Comment newParent = new Comment("new-parent");

        // Simulate a state where the original node has not yet been added to a parent.
        // A siblingIndex of -1 indicates it's not part of a child node list.
        // This ensures the original node's state doesn't affect the clone's parenting.
        originalNode.siblingIndex = -1;

        // Act: Clone the original node, providing the new parent.
        // The doClone() method is protected, so this test must reside in the same package.
        LeafNode clonedNode = originalNode.doClone(newParent);

        // Assert: The cloned node should have the new parent assigned.
        assertTrue("The cloned node should report having a parent.", clonedNode.hasParent());
        assertSame("The cloned node's parent should be the instance provided during cloning.", newParent, clonedNode.parent());
    }
}