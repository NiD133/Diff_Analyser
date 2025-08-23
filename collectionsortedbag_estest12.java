package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * This test case focuses on the behavior of {@link CollectionSortedBag}
 * when decorating a {@link TreeBag} that uses natural ordering.
 */
public class CollectionSortedBag_ESTestTest12 extends CollectionSortedBag_ESTest_scaffolding {

    /**
     * Tests that attempting to remove a null element from a CollectionSortedBag
     * throws a NullPointerException if the underlying decorated bag (a TreeBag
     * with natural ordering) does not support nulls.
     * <p>
     * The TreeBag, when constructed without a specific comparator, relies on
     * the natural ordering of its elements. Natural ordering does not permit
     * null elements, and operations involving null will result in a
     * NullPointerException. This test verifies that the decorator correctly
     * propagates this exception from the decorated bag.
     */
    @Test(expected = NullPointerException.class)
    public void removeNullFromBagWithNaturalOrderingShouldThrowNPE() {
        // Arrange: Create a CollectionSortedBag decorating a TreeBag.
        // The default TreeBag() constructor creates a bag that uses natural ordering
        // and consequently does not allow null elements.
        final SortedBag<Integer> backingBag = new TreeBag<>();
        final SortedBag<Integer> collectionSortedBag = new CollectionSortedBag<>(backingBag);

        // Act & Assert: Attempting to remove null should throw a NullPointerException.
        // The assertion is handled by the 'expected' attribute of the @Test annotation.
        collectionSortedBag.remove(null);
    }
}