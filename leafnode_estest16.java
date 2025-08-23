package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the LeafNode class.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling removeAttr on a LeafNode that has a parent
     * returns the node itself and does not detach it from its parent.
     */
    @Test
    public void removeAttrOnNodeWithParentShouldReturnNodeAndRetainParent() {
        // Arrange: Create a LeafNode (a CDataNode) and a parent Document.
        CDataNode originalNode = new CDataNode("cdata content");
        Document parentDocument = new Document("http://example.com/");

        // Clone the node and assign the parent. The doClone method handles this.
        LeafNode nodeWithParent = originalNode.doClone(parentDocument);
        assertTrue("Pre-condition failed: Node should have a parent after cloning.", nodeWithParent.hasParent());

        // Act: Attempt to remove an attribute that does not exist.
        Node resultNode = nodeWithParent.removeAttr("nonExistentAttribute");

        // Assert: The method should return the same node instance, and it must still have its parent.
        assertSame("removeAttr should return the same node instance.", nodeWithParent, resultNode);
        assertTrue("Node should still have a parent after removeAttr is called.", resultNode.hasParent());
        assertSame("The node's parent should be unchanged.", parentDocument, resultNode.parent());
    }
}