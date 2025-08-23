package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Iterator;
import org.junit.Test;

/**
 * Contains tests for {@link ObjectGraphIterator}.
 */
public class ObjectGraphIteratorImprovedTest {

    /**
     * Tests that calling remove() before a call to next() throws an IllegalStateException.
     * This behavior is specified by the java.util.Iterator interface contract.
     */
    @Test
    public void removeShouldThrowIllegalStateExceptionWhenNextHasNotBeenCalled() {
        // Arrange: Create a simple ObjectGraphIterator.
        // An iterator with a single element is sufficient for this test.
        final Iterator<String> rootIterator = Collections.singleton("root").iterator();
        final ObjectGraphIterator<String> graphIterator = new ObjectGraphIterator<>(rootIterator);

        // Act & Assert: Verify that calling remove() immediately throws the expected exception.
        final IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            graphIterator::remove
        );

        // Assert that the exception message is correct, ensuring the right error condition was triggered.
        assertEquals("Iterator remove() cannot be called at this time", exception.getMessage());
    }
}