package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Test suite for {@link CollectionSortedBag}.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that addAll() throws a ClassCastException when attempting to add a
     * non-comparable element to a bag that relies on natural ordering.
     */
    @Test(expected = ClassCastException.class)
    public void addAllShouldThrowClassCastExceptionWhenAddingNonComparableToBagWithNaturalOrdering() {
        // Arrange: Create a CollectionSortedBag that decorates a TreeBag.
        // The default TreeBag constructor means it relies on the natural ordering
        // of its elements (i.e., they must implement Comparable).
        final SortedBag<Object> naturalOrderingBag = new CollectionSortedBag<>(new TreeBag<>());

        // Arrange: Create a source collection containing an element that does not
        // implement Comparable. A plain Object is a perfect example.
        final Collection<Object> sourceWithNonComparableElement = new ArrayList<>();
        sourceWithNonComparableElement.add(new Object());

        // Act & Assert: When addAll is called, the underlying TreeBag will try to
        // cast the new element to Comparable to determine its position. This fails
        // and is expected to throw a ClassCastException.
        naturalOrderingBag.addAll(sourceWithNonComparableElement);
    }
}