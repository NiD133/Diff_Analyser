package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the LeafNode class, focusing on attribute handling.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling removeAttr on a LeafNode that has no attributes
     * does not cause an error and leaves the node unchanged.
     */
    @Test
    public void removeAttrOnNodeWithNoAttributesDoesNothing() {
        // Arrange: Create a LeafNode (a DataNode instance) with no attributes.
        DataNode dataNode = new DataNode("Sample data content");
        String keyToRemove = "class";

        // Pre-condition: Verify the node has no attributes initially.
        assertFalse("Node should not have attributes before the test", dataNode.hasAttributes());

        // Act: Attempt to remove an attribute that does not exist.
        Node resultNode = dataNode.removeAttr(keyToRemove);

        // Assert: The node's state should remain unchanged.
        assertFalse("Node should still have no attributes after the operation", dataNode.hasAttributes());
        assertSame("removeAttr should return the same node instance for chaining", dataNode, resultNode);
    }
}