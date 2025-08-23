package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Tests for {@link CollectionBag} focusing on its behavior when decorating
 * an unmodifiable underlying bag.
 */
public class CollectionBagTest { // Renamed from CollectionBag_ESTestTest16 for clarity

    /**
     * Verifies that calling addAll() on a CollectionBag throws an
     * UnsupportedOperationException if the underlying decorated bag is unmodifiable.
     * The CollectionBag should delegate the call, which then fails.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllThrowsExceptionWhenDecoratingUnmodifiableBag() {
        // Arrange: Create a CollectionBag that decorates an unmodifiable bag.
        final Bag<Integer> sourceBag = new TreeBag<>();
        sourceBag.add(512);

        final SortedBag<Integer> unmodifiableBag = UnmodifiableSortedBag.unmodifiableSortedBag((SortedBag<Integer>) sourceBag);

        // The instance under test
        final Bag<Integer> collectionBag = new CollectionBag<>(unmodifiableBag);

        // Act & Assert: Attempting to add elements should throw an exception.
        // The exception is expected by the @Test annotation.
        collectionBag.addAll(unmodifiableBag);
    }
}