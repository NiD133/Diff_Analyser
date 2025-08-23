package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Iterator;
import org.junit.Test;

/**
 * Unit tests for {@link BoundedIterator}.
 */
public class BoundedIteratorTest {

    /**
     * Tests that calling remove() before next() throws an IllegalStateException,
     * as required by the Iterator contract.
     */
    @Test
    public void removeShouldThrowIllegalStateExceptionWhenNextHasNotBeenCalled() {
        // Arrange
        // Create a mock for the underlying iterator. No behavior needs to be stubbed
        // for this test case, as the constructor with offset=0 does not use it.
        @SuppressWarnings("unchecked") // Mockito's mock() returns a raw type
        final Iterator<Object> mockUnderlyingIterator = mock(Iterator.class);

        // Create the iterator under test. We use a zero offset and a non-zero max
        // to create the simplest test case.
        final BoundedIterator<Object> boundedIterator =
                new BoundedIterator<>(mockUnderlyingIterator, 0L, 10L);

        // Act & Assert
        // We expect an IllegalStateException because next() has not yet been called.
        try {
            boundedIterator.remove();
            fail("An IllegalStateException should have been thrown.");
        } catch (final IllegalStateException e) {
            // Verify that the exception has the expected message.
            assertEquals("remove() cannot be called before calling next()", e.getMessage());
        }
    }
}