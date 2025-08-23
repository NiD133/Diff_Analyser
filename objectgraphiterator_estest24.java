package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link ObjectGraphIterator} focusing on its interaction with underlying iterators.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that the iterator fails fast with a ConcurrentModificationException
     * if the underlying source collection is modified after the iterator has been created.
     * This is the standard, expected behavior for Java collection iterators.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void hasNextShouldThrowExceptionWhenUnderlyingCollectionIsModified() {
        // Arrange: Create an ObjectGraphIterator from a list's iterator.
        final List<Integer> sourceList = new LinkedList<>();
        final Iterator<Integer> listIterator = sourceList.iterator();
        final ObjectGraphIterator<Integer> graphIterator = new ObjectGraphIterator<>(listIterator);

        // Act: Modify the source list directly. This invalidates the original listIterator.
        sourceList.add(42);

        // Assert: The next operation on the graphIterator, which delegates to the
        // invalidated listIterator, should throw a ConcurrentModificationException.
        graphIterator.hasNext();
    }
}