package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link NodeIterator} class.
 */
public class NodeIteratorTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException
     * when the root node provided is null, as this is an invalid state.
     */
    @Test
    public void constructorThrowsExceptionForNullNode() {
        try {
            // Attempt to create an iterator with a null root node.
            new NodeIterator<>(null, Node.class);
            
            // If the constructor does not throw an exception, this test should fail.
            fail("Expected an IllegalArgumentException to be thrown for a null root node.");
        } catch (IllegalArgumentException e) {
            // Success: The expected exception was caught.
            // We can also verify the message to ensure it's the one we expect from our validation logic.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}