package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Tests for {@link CollectionSortedBag} focusing on exception handling with incompatible types.
 */
public class CollectionSortedBag_ESTestTest26 extends CollectionSortedBag_ESTest_scaffolding {

    /**
     * Verifies that adding a non-comparable element to a CollectionSortedBag
     * that is backed by a natural-ordering SortedBag (like TreeBag)
     * throws a ClassCastException.
     */
    @Test(expected = ClassCastException.class)
    public void addWithCount_whenElementIsNotComparable_shouldThrowClassCastException() {
        // Arrange: Create a CollectionSortedBag decorating a TreeBag.
        // A TreeBag requires its elements to be Comparable or to be configured with a Comparator.
        SortedBag<Object> underlyingBag = new TreeBag<>();
        SortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(underlyingBag);

        // Create an element that does not implement the Comparable interface.
        // A new Object() or, as in the original test, another Bag instance works well.
        Object nonComparableElement = new TreeBag<>();

        // Act & Assert: Attempting to add the non-comparable element should fail.
        // The underlying TreeBag will throw a ClassCastException when it tries to
        // compare the new element to place it in sorted order.
        collectionSortedBag.add(nonComparableElement, 1000);
    }
}