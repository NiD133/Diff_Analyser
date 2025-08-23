package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for {@link BoundedIterator}.
 */
public class BoundedIteratorTest {

    /**
     * Tests that a BoundedIterator propagates a ConcurrentModificationException
     * if the underlying collection is modified after the iterator is created.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void testNextThrowsExceptionWhenUnderlyingCollectionIsModified() {
        // Arrange: Create a BoundedIterator for a list.
        final List<String> sourceList = new LinkedList<>();
        sourceList.add("element1");
        final Iterator<String> sourceIterator = sourceList.iterator();

        // The BoundedIterator wraps the source iterator.
        final BoundedIterator<String> boundedIterator = new BoundedIterator<>(sourceIterator, 0L, 5L);

        // Act: Structurally modify the source list after the iterator has been created.
        // This action invalidates the sourceIterator.
        sourceList.add("new element");

        // Assert: Calling next() on the bounded iterator should attempt to call next() on the
        // invalidated source iterator. This is expected to throw a
        // ConcurrentModificationException, which is verified by the @Test annotation.
        boundedIterator.next();
    }
}