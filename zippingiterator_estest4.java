package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link ZippingIterator}.
 * This test focuses on the behavior of the remove() method when the underlying collection is modified.
 */
public class ZippingIteratorTest {

    /**
     * Verifies that calling remove() on a ZippingIterator throws a
     * ConcurrentModificationException if the underlying collection of the
     * source iterator has been modified since the last call to next().
     */
    @Test(expected = ConcurrentModificationException.class)
    public void removeShouldThrowConcurrentModificationExceptionWhenUnderlyingCollectionIsModified() {
        // Arrange: Create a list, get an iterator from it, and wrap it in a ZippingIterator.
        final List<Integer> sourceList = new LinkedList<>();
        sourceList.add(100);

        final Iterator<Integer> listIterator = sourceList.iterator();

        // The ZippingIterator will delegate its remove() call to the underlying iterator.
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(listIterator);

        // Call next() to ensure the iterator is in a state where remove() can be called.
        zippingIterator.next();

        // Act: Modify the source collection directly, not through the iterator.
        // This invalidates the state of the original iterator.
        sourceList.clear();

        // Assert: The subsequent call to remove() on the ZippingIterator should fail.
        // This is because it delegates to the original iterator, which detects the concurrent modification.
        zippingIterator.remove(); // This line is expected to throw the exception.
    }
}