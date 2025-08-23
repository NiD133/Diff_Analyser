package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link CollectionSortedBag} decorator.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that the {@code collectionSortedBag()} factory method correctly
     * decorates an empty bag, resulting in a new empty bag.
     */
    @Test
    public void collectionSortedBagFactoryMethod_withEmptyBag_returnsEmptyBag() {
        // Arrange: Create an empty source bag. A TreeBag is a standard SortedBag implementation.
        final SortedBag<Object> emptySourceBag = new TreeBag<>();

        // Act: Decorate the source bag using the factory method under test.
        final SortedBag<Object> decoratedBag = CollectionSortedBag.collectionSortedBag(emptySourceBag);

        // Assert: The resulting decorated bag should also be empty.
        assertTrue("The decorated bag should be empty", decoratedBag.isEmpty());
        assertEquals("The size of the decorated bag should be 0", 0, decoratedBag.size());
    }
}