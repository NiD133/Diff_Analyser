package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Iterator;
import org.junit.Test;

/**
 * Tests for {@link ObjectGraphIterator}.
 * This class focuses on the behavior of the remove() method.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that calling remove() before next() has been called results in an
     * IllegalStateException, as per the Iterator contract.
     */
    @Test
    public void removeShouldThrowIllegalStateExceptionWhenNextHasNotBeenCalled() {
        // Arrange: Create an ObjectGraphIterator. The root iterator can be null
        // as its state is irrelevant before the first call to next().
        final ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>((Iterator<Integer>) null);

        // Act & Assert: Expect an IllegalStateException when remove() is called.
        final IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            iterator::remove // This is a method reference for () -> iterator.remove()
        );

        // Assert (optional but good practice): Verify the exception message is helpful.
        assertEquals("Iterator remove() cannot be called at this time", exception.getMessage());
    }
}