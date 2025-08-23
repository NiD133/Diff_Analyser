package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the LeafNode abstract class, focusing on core value management.
 */
public class LeafNodeTest {

    /**
     * Verifies that the core value of a LeafNode can be set to null.
     * This is tested via a concrete subclass, DataNode.
     */
    @Test
    public void coreValue_shouldReturnNull_whenDataIsSetToNull() {
        // Arrange: Create a DataNode (a concrete LeafNode) with non-null initial data.
        DataNode dataNode = new DataNode("Initial data");

        // Act: Set the node's internal data to null using its specific setter.
        dataNode.setWholeData(null);

        // Assert: Verify that the coreValue(), a method from the LeafNode parent, now returns null.
        assertNull("The core value should be null after being set to null.", dataNode.coreValue());
    }
}