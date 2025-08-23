package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the LeafNode class, focusing on cloning behavior.
 */
public class LeafNodeTest {

    /**
     * Verifies that the doClone(parent) method correctly assigns the specified
     * parent to the newly created clone.
     */
    @Test
    public void doCloneShouldSetParentOnClonedNode() {
        // Arrange: Create an original node and a new parent for the clone.
        // DataNode is a concrete implementation of the abstract LeafNode.
        DataNode originalNode = new DataNode("Original data content");
        
        // Any Node can be a parent. Using a Comment node as an example.
        Comment newParent = new Comment("This is the parent node");

        // Act: Clone the original node, assigning the new parent during the process.
        // The doClone method is protected, so this test must be in the same package.
        LeafNode clonedNode = originalNode.doClone(newParent);

        // Assert: The cloned node should have the new parent assigned to it.
        assertTrue("The cloned node should have a parent.", clonedNode.hasParent());
        assertSame("The cloned node's parent should be the exact instance we provided.", newParent, clonedNode.parent());
    }
}