package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Locale;

/**
 * Contains tests for {@link CollectionSortedBag}, focusing on its decorator behavior.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that a modification attempt on a CollectionSortedBag that wraps an unmodifiable bag
     * correctly throws an UnsupportedOperationException.
     *
     * This verifies that the decorator correctly delegates the modification call,
     * preserving the unmodifiable nature of the decorated bag.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addWithCountShouldThrowExceptionWhenDecoratingUnmodifiableBag() {
        // Arrange: Create a CollectionSortedBag that decorates an unmodifiable bag.
        final SortedBag<Locale.FilteringMode> unmodifiableBag =
                UnmodifiableSortedBag.unmodifiableSortedBag(new TreeBag<>());

        final SortedBag<Locale.FilteringMode> collectionBag = new CollectionSortedBag<>(unmodifiableBag);

        final Locale.FilteringMode elementToAdd = Locale.FilteringMode.EXTENDED_FILTERING;
        final int count = 5;

        // Act: Attempt to add an element to the decorated bag.
        // Assert: The test expects an UnsupportedOperationException to be thrown by the line below.
        collectionBag.add(elementToAdd, count);
    }
}