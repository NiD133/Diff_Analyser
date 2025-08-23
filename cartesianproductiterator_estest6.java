package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertFalse;

import java.util.NoSuchElementException;
import org.junit.Test;

/**
 * Unit tests for {@link CartesianProductIterator}.
 */
public class CartesianProductIteratorTest {

    @Test(expected = NoSuchElementException.class)
    public void nextOnIteratorWithNoIterablesShouldThrowException() {
        // Arrange: Create an iterator with no input iterables.
        // This results in an empty Cartesian product.
        final CartesianProductIterator<Object> iterator = new CartesianProductIterator<>();

        // Assert: The iterator should correctly report that it has no elements.
        assertFalse("Iterator should be empty when no iterables are provided", iterator.hasNext());

        // Act: Calling next() on an empty iterator should throw the expected exception.
        iterator.next();
    }
}