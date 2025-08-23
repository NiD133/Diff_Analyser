package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link NodeIterator}.
 * This focuses on the behavior of the remove() method.
 */
public class NodeIteratorTest {

    /**
     * Verifies that calling remove() on a new iterator, before next() has been called,
     * throws an exception as per the Iterator contract.
     */
    @Test
    public void removeShouldThrowExceptionWhenCalledBeforeNext() {
        // Arrange: Create an iterator over an empty document.
        // The iterator's 'current' element will be null.
        Document emptyDoc = new Document("");
        NodeIterator<Node> iterator = new NodeIterator<>(emptyDoc, Node.class);

        // Act & Assert: Expect an exception when calling remove() without a preceding next().
        // Jsoup's implementation throws an IllegalArgumentException in this case.
        IllegalArgumentException exception = assertThrows(
            "Calling remove() before next() should throw an exception.",
            IllegalArgumentException.class,
            iterator::remove
        );

        // Optional: Verify the exception message for more precise testing.
        assertEquals("next() must be called before remove().", exception.getMessage());
    }
}