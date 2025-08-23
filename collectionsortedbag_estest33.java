package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Contains tests for {@link CollectionSortedBag}, focusing on its interaction
 * with underlying sorted collections that have specific constraints.
 */
public class CollectionSortedBag_ESTestTest33 {

    /**
     * Tests that a ClassCastException is thrown when attempting to add a
     * non-comparable object to a CollectionSortedBag that wraps a TreeBag.
     *
     * The TreeBag, by default, uses the natural ordering of its elements,
     * which requires them to implement the Comparable interface. The test verifies
     * that this constraint from the underlying bag is correctly propagated.
     */
    @Test(expected = ClassCastException.class)
    public void addShouldThrowClassCastExceptionWhenElementIsNotComparable() {
        // Arrange: Create a CollectionSortedBag decorating a TreeBag.
        // A TreeBag without a custom comparator requires its elements to be Comparable.
        SortedBag<Object> underlyingBag = new TreeBag<>();
        SortedBag<Object> bagUnderTest = new CollectionSortedBag<>(underlyingBag);

        // Act & Assert: Attempting to add a plain Object (which is not Comparable)
        // should fail with a ClassCastException when the underlying TreeBag
        // tries to determine its position in the sorted structure.
        bagUnderTest.add(new Object());
    }
}