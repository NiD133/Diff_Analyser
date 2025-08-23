package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link BoundedIterator} focusing on exception handling scenarios.
 */
public class BoundedIteratorExceptionTest {

    /**
     * Tests that the BoundedIterator constructor throws a ConcurrentModificationException
     * if the underlying collection is modified after the iterator is created but before
     * the BoundedIterator is initialized.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void testConstructorThrowsConcurrentModificationException() {
        // Arrange: Create a list, get an iterator, and then modify the list.
        final List<Object> list = new LinkedList<>();
        final Iterator<Object> underlyingIterator = list.iterator();
        list.add("This modification invalidates the iterator");

        // Act: Construct a BoundedIterator with an offset greater than 0.
        // The constructor attempts to advance the invalid iterator to the offset,
        // which should trigger the ConcurrentModificationException.
        new BoundedIterator<>(underlyingIterator, 1L, 10L);

        // Assert: The test passes if the expected exception is thrown.
    }
}