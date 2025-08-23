package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Tests for {@link CollectionSortedBag}, focusing on argument validation.
 */
public class CollectionSortedBag_ESTestTest18 {

    /**
     * Verifies that calling addAll() with a null collection throws a NullPointerException.
     * <p>
     * The {@link CollectionSortedBag} is a decorator, so it is expected to delegate
     * this call to the underlying bag, which should reject a null argument.
     */
    @Test(expected = NullPointerException.class)
    public void addAllWithNullCollectionShouldThrowNullPointerException() {
        // Arrange: Create a CollectionSortedBag decorating a standard TreeBag.
        final SortedBag<Object> decoratedBag = new TreeBag<>();
        final SortedBag<Object> collectionBag = new CollectionSortedBag<>(decoratedBag);

        // Act: Attempt to add a null collection. This action is expected to throw.
        collectionBag.addAll(null);

        // Assert: The 'expected' parameter of the @Test annotation handles the assertion.
        // The test will pass only if a NullPointerException is thrown.
    }
}