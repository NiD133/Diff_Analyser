package org.jsoup.nodes;

import org.junit.Test;

/**
 * Tests for the {@link LeafNode} class, focusing on attribute handling.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling the attr(key, value) method with a null key
     * throws an IllegalArgumentException, as per the API contract.
     */
    @Test(expected = IllegalArgumentException.class)
    public void attrWithNullKeyThrowsIllegalArgumentException() {
        // Arrange: Create an instance of a LeafNode subclass.
        // The specific subclass (CDataNode) and its content are not critical for this test.
        LeafNode node = new CDataNode("");

        // Act: Attempt to set an attribute with a null key.
        // This action is expected to trigger the exception.
        node.attr(null, "any-value");

        // Assert: The test passes if an IllegalArgumentException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}