package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Tests for {@link CollectionBag}.
 */
public class CollectionBagTest {

    /**
     * Tests that remove() throws a ClassCastException when an object of an
     * incompatible type is passed to a CollectionBag that decorates a sorted bag.
     * The exception originates from the decorated bag (TreeBag), which cannot
     * compare the incompatible object with its existing elements.
     */
    @Test(expected = ClassCastException.class)
    public void testRemoveWithIncompatibleTypeFromDecoratedSortedBagThrowsException() {
        // Arrange: Create a CollectionBag decorating a TreeBag, which is sorted
        // and expects elements of type Integer.
        final Bag<Integer> decoratedBag = new TreeBag<>();
        final Bag<Integer> collectionBag = new CollectionBag<>(decoratedBag);
        final Object incompatibleObject = new Object();

        // Act & Assert: Attempting to remove an object of an incompatible type.
        // This should throw a ClassCastException because the underlying TreeBag
        // cannot cast the object to a Comparable type (like Integer) for comparison.
        collectionBag.remove(incompatibleObject);
    }
}