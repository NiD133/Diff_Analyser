package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Test cases for {@link ObjectGraphIterator}.
 * This class focuses on specific behaviors of the iterator.
 */
public class ObjectGraphIteratorTest {

    /**
     * Verifies that calling next() on an ObjectGraphIterator initialized with an
     * empty root iterator correctly throws a NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowExceptionWhenRootIteratorIsEmpty() {
        // Arrange: Create an ObjectGraphIterator with an empty iterator as its root.
        final Iterator<Object> emptyRootIterator = Collections.emptyIterator();
        final ObjectGraphIterator<Object> graphIterator = new ObjectGraphIterator<>(emptyRootIterator);

        // Act & Assert: Calling next() on the iterator is expected to throw.
        graphIterator.next();
    }
}