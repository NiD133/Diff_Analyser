package org.apache.commons.collections4.iterators;

import org.junit.Test;

/**
 * Tests for {@link CartesianProductIterator}.
 * This class focuses on specific behaviors not covered in other tests.
 */
public class CartesianProductIteratorTest {

    /**
     * Tests that calling the remove() method throws an UnsupportedOperationException,
     * as specified in the iterator's contract.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void removeShouldThrowUnsupportedOperationException() {
        // The iterator is created with no iterables. The behavior of remove()
        // should be the same regardless of the input iterables.
        final CartesianProductIterator<Object> iterator = new CartesianProductIterator<>();

        // Action: Attempt to call the unsupported remove() operation.
        // The @Test(expected=...) annotation will cause the test to pass
        // if this line throws an UnsupportedOperationException.
        iterator.remove();
    }
}