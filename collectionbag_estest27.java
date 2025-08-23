package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Tests the behavior of {@link CollectionBag} when it decorates an unmodifiable bag.
 */
// The original test extended a scaffolding class, which is preserved here.
public class CollectionBag_ESTestTest27 extends CollectionBag_ESTest_scaffolding {

    /**
     * Verifies that add() throws an UnsupportedOperationException when the CollectionBag
     * is backed by an unmodifiable bag.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addShouldThrowExceptionWhenDecoratingUnmodifiableBag() {
        // Arrange: Create a CollectionBag that wraps an unmodifiable bag.
        final SortedBag<Integer> underlyingBag = new TreeBag<>();
        final SortedBag<Integer> unmodifiableBag = UnmodifiableSortedBag.unmodifiableSortedBag(underlyingBag);
        final Bag<Integer> collectionBag = new CollectionBag<>(unmodifiableBag);

        // Act: Attempt to add an element to the decorated bag.
        // This action is expected to throw an UnsupportedOperationException,
        // which is handled by the @Test(expected=...) annotation.
        collectionBag.add(-300);
    }
}