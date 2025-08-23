package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.LinkedList;

/**
 * Tests for {@link CollectionSortedBag}.
 * This class focuses on how the decorator interacts with the underlying sorted bag.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that add() propagates a ClassCastException from the underlying bag.
     *
     * <p>A TreeBag requires its elements to be Comparable. This test confirms that
     * when CollectionSortedBag decorates a TreeBag, an attempt to add a
     * non-Comparable element correctly results in a ClassCastException.</p>
     */
    @Test(expected = ClassCastException.class)
    public void addShouldPropagateClassCastExceptionFromUnderlyingTreeBag() {
        // Arrange: Create a CollectionSortedBag decorating a TreeBag.
        // A TreeBag requires elements to be Comparable, but LinkedList is not.
        final SortedBag<LinkedList<Object>> underlyingBag = new TreeBag<>();
        final SortedBag<LinkedList<Object>> collectionSortedBag = new CollectionSortedBag<>(underlyingBag);
        final LinkedList<Object> nonComparableElement = new LinkedList<>();

        // Act: Attempt to add the non-Comparable element.
        // This action is expected to throw a ClassCastException from the underlying TreeBag.
        collectionSortedBag.add(nonComparableElement, 10);
    }
}