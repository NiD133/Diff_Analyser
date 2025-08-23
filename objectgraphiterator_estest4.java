package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link ObjectGraphIterator} focusing on its fail-fast behavior.
 */
public class ObjectGraphIteratorTest {

    /**
     * Verifies that the ObjectGraphIterator throws a ConcurrentModificationException
     * if the underlying collection is modified after the iterator has been created.
     * This confirms its fail-fast behavior, which is inherited from the wrapped iterator.
     */
    @Test
    public void iteratorShouldThrowExceptionOnConcurrentModification() {
        // Arrange: Create a list, get an iterator for it, and then wrap that
        // iterator with an ObjectGraphIterator.
        final List<Integer> list = new LinkedList<>();
        final ListIterator<Integer> underlyingIterator = list.listIterator();
        final ObjectGraphIterator<Integer> graphIterator = new ObjectGraphIterator<>(underlyingIterator);

        // Act: Modify the underlying list directly. This action invalidates the
        // 'underlyingIterator' that the 'graphIterator' depends on.
        list.add(42);

        // Assert: Expect a ConcurrentModificationException when using the graphIterator.
        // The graphIterator delegates to the now-invalidated underlying iterator,
        // which should detect the modification and fail fast.
        assertThrows(ConcurrentModificationException.class, () -> graphIterator.hasNext());
    }
}