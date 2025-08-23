package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertThrows;

import java.util.NoSuchElementException;
import org.junit.Test;

/**
 * Contains tests for the {@link FilterListIterator} class.
 */
public class FilterListIteratorTest {

    /**
     * Verifies that calling previous() on a FilterListIterator that has not been
     * initialized with an underlying iterator correctly throws a NoSuchElementException.
     * This simulates iterating backward over an empty sequence.
     */
    @Test
    public void previousShouldThrowNoSuchElementExceptionForUninitializedIterator() {
        // Arrange: Create a FilterListIterator using its default constructor, which
        // leaves the underlying iterator as null.
        final FilterListIterator<Object> emptyIterator = new FilterListIterator<>();

        // Act & Assert: Expect a NoSuchElementException when previous() is called,
        // as there are no elements to retrieve.
        assertThrows(NoSuchElementException.class, () -> emptyIterator.previous());
    }
}