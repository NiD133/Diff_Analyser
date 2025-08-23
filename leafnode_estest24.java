package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for the abstract LeafNode class, verifying its attribute-handling contract.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling removeAttr() with a null key throws an IllegalArgumentException.
     * This is the expected behavior, as attribute keys must not be null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeAttrWithNullKeyShouldThrowIllegalArgumentException() {
        // Arrange: Create an instance of a LeafNode subclass.
        // The specific subclass (e.g., CDataNode) and its content are not important for this test.
        LeafNode node = new CDataNode("some data");

        // Act: Attempt to remove an attribute using a null key.
        // Assert: The @Test(expected) annotation asserts that an IllegalArgumentException is thrown.
        node.removeAttr(null);
    }
}