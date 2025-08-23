package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Tests for {@link CollectionSortedBag}.
 */
public class CollectionSortedBagTest {

    /**
     * A simple helper class that does not implement Comparable, used for testing.
     */
    private static class NonComparableObject {
    }

    @Test(expected = ClassCastException.class)
    public void addNonComparableToUnderlyingTreeBagShouldThrowException() {
        // Arrange
        // A TreeBag without a custom comparator relies on the natural ordering of its elements.
        // This means elements must implement the Comparable interface.
        final SortedBag<Object> underlyingTreeBag = new TreeBag<>();
        final SortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(underlyingTreeBag);
        final Object nonComparableObject = new NonComparableObject();

        // Act
        // Attempting to add an object that is not Comparable to a TreeBag will cause a
        // ClassCastException when the bag tries to sort it.
        collectionSortedBag.add(nonComparableObject);

        // Assert
        // A ClassCastException is expected, as declared in the @Test annotation.
    }
}