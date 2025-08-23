package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Contains tests for the {@link CollectionBag} decorator.
 */
public class CollectionBagTest {

    /**
     * Tests that an attempt to add a null element to a CollectionBag propagates
     * the exception from the underlying decorated bag. In this case, the backing
     * TreeBag does not support null elements.
     */
    @Test(expected = NullPointerException.class)
    public void testAddNullToBagBackedByTreeBagShouldThrowException() {
        // Arrange: Create a TreeBag (which doesn't allow nulls) and
        // decorate it with a CollectionBag.
        final Bag<Integer> backingTreeBag = new TreeBag<>();
        final Bag<Integer> collectionBag = new CollectionBag<>(backingTreeBag);

        // Act: Attempt to add a null element.
        // This action is expected to throw a NullPointerException because the
        // backing TreeBag will reject the null.
        collectionBag.add(null);

        // Assert: The @Test(expected) annotation asserts that a
        // NullPointerException was thrown.
    }
}