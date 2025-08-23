package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the {@link NodeIterator} class.
 */
public class NodeIteratorTest {

    /**
     * Verifies that the {@link NodeIterator#from(Node)} factory method throws an
     * IllegalArgumentException when passed a null Node, as the starting node
     * for iteration must not be null.
     */
    @Test
    public void fromWithNullNodeThrowsIllegalArgumentException() {
        // The contract of the `from()` factory method, via `Validate.notNull()`,
        // requires a non-null root node to begin iteration.
        // This test ensures that passing null results in the expected exception.

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(
            "The factory method should throw an exception for a null input.",
            IllegalArgumentException.class,
            () -> NodeIterator.from(null)
        );

        // Verify that the exception message is helpful and matches the contract.
        assertEquals("Object must not be null", thrown.getMessage());
    }
}