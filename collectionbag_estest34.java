package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Contains tests for the {@link CollectionBag} decorator, focusing on its
 * interaction with the decorated bag's constraints.
 */
public class CollectionBagTest {

    /**
     * Tests that a CollectionBag decorator correctly propagates exceptions from the
     * underlying decorated bag. Specifically, it verifies that attempting to add a
     * null element to a CollectionBag wrapping a TreeBag (which does not permit
     * nulls) results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testAddNullToBagBackedByTreeBagShouldThrowException() {
        // Arrange: Create a CollectionBag that decorates a TreeBag.
        // A TreeBag is a sorted bag and does not allow null elements.
        final Bag<Integer> treeBag = new TreeBag<>();
        final Bag<Integer> collectionBag = new CollectionBag<>(treeBag);

        // Act: Attempt to add a null element. This action is expected to throw
        // a NullPointerException because the underlying TreeBag will reject it.
        collectionBag.add(null, 2412);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}