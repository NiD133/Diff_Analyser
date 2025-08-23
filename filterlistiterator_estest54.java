package org.apache.commons.collections4.iterators;

import org.junit.Test;
import java.util.NoSuchElementException;

/**
 * Unit tests for {@link FilterListIterator}.
 */
public class FilterListIteratorTest {

    /**
     * Tests that calling next() on an iterator created with the default constructor
     * throws a NoSuchElementException, as it has no underlying iterator to traverse.
     */
    @Test(expected = NoSuchElementException.class)
    public void nextOnDefaultConstructedIteratorShouldThrowNoSuchElementException() {
        // Arrange: Create a FilterListIterator using its default constructor.
        // This iterator is not initialized with an underlying list or a predicate.
        final FilterListIterator<Object> uninitializedIterator = new FilterListIterator<>();

        // Act: Attempting to get the next element should throw an exception
        // because there is no underlying collection to iterate over.
        uninitializedIterator.next();

        // Assert: The @Test(expected=...) annotation handles the exception assertion.
    }
}