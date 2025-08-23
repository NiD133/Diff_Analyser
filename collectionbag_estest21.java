package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * This test case has been improved to clearly demonstrate the behavior of CollectionBag
 * when it decorates an unmodifiable bag.
 */
public class CollectionBag_ESTestTest21 {

    /**
     * Tests that a CollectionBag wrapping an unmodifiable bag correctly throws
     * an UnsupportedOperationException when a modification method like add() is called.
     * This verifies that the decorator correctly propagates the unmodifiable nature
     * of the underlying collection.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addShouldThrowExceptionWhenDecoratingUnmodifiableBag() {
        // Arrange: Create a CollectionBag that decorates an unmodifiable bag.
        final SortedBag<Object> unmodifiableBag =
                UnmodifiableSortedBag.unmodifiableSortedBag(new TreeBag<>());
        final Bag<Object> collectionBag = new CollectionBag<>(unmodifiableBag);

        // Act: Attempt to add an element to the CollectionBag.
        // This action is expected to throw an UnsupportedOperationException
        // because the underlying bag is unmodifiable.
        collectionBag.add("an element", 512);

        // Assert: The @Test(expected) annotation handles the assertion that an
        // UnsupportedOperationException is thrown.
    }
}