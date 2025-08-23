package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the LeafNode class.
 * Note: The original test was part of a larger, generated suite. This version is a standalone,
 * human-readable equivalent of the provided test case.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling the attr(key) method with a null key
     * throws an IllegalArgumentException, as expected by the method's contract.
     */
    @Test
    public void attrWithNullKeyShouldThrowIllegalArgumentException() {
        // Arrange: Create a LeafNode instance. CDataNode is a concrete implementation.
        LeafNode node = new CDataNode("This is some data.");

        // Act & Assert: Attempt to get an attribute with a null key and expect an exception.
        try {
            node.attr(null);
            fail("Expected an IllegalArgumentException to be thrown for a null attribute key, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, confirming the input validation.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}