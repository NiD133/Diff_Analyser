package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import java.util.ConcurrentModificationException;

/**
 * Test suite for {@link CollectionBag}.
 */
public class CollectionBagTest {

    /**
     * Tests that attempting to add a bag's contents to itself via {@code addAll}
     * results in a {@link ConcurrentModificationException}.
     * <p>
     * This is expected behavior when a collection is modified while it is being iterated over,
     * which occurs when a bag is the source and destination for an {@code addAll} operation.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void addAll_whenAddingBagToItself_throwsConcurrentModificationException() {
        // Arrange
        // Create a bag, add an element, and wrap it in a CollectionBag decorator.
        Bag<String> underlyingBag = new TreeBag<>();
        Bag<String> collectionBag = new CollectionBag<>(underlyingBag);
        collectionBag.add("An element");

        // Act
        // Attempt to add all elements from the underlying bag back into the decorated bag.
        // This will fail because the addAll operation iterates over the underlying bag
        // while simultaneously trying to modify it.
        collectionBag.addAll(underlyingBag);
    }
}