package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link LeafNode} class, focusing on its exception-handling behavior.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling absUrl() with an empty string for the attribute key
     * throws an IllegalArgumentException, as the key must not be empty.
     */
    @Test
    public void absUrlWithEmptyKeyThrowsIllegalArgumentException() {
        // Arrange: Create a concrete instance of a LeafNode. The node's content is irrelevant.
        LeafNode node = new CDataNode("some data");

        try {
            // Act: Call the method under test with an invalid (empty) key.
            node.absUrl("");
            
            // Assert: If the method does not throw an exception, this test should fail.
            fail("Expected an IllegalArgumentException to be thrown for an empty key.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the exception has the expected message.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}