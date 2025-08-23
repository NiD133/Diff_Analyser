package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Unit tests for {@link CollectionSortedBag}.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that attempting to remove an element from an empty bag returns false.
     */
    @Test
    public void remove_fromEmptyBag_shouldReturnFalse() {
        // Arrange: Create an empty CollectionSortedBag.
        // A TreeBag is used as the underlying sorted collection.
        final SortedBag<String> underlyingBag = new TreeBag<>();
        final SortedBag<String> collectionSortedBag = new CollectionSortedBag<>(underlyingBag);

        // Pre-condition check: Ensure the bag is indeed empty.
        assertTrue("The bag should be empty before the test", collectionSortedBag.isEmpty());

        // Act: Attempt to remove a non-existent element.
        final boolean wasRemoved = collectionSortedBag.remove("anyObject");

        // Assert: The remove operation should fail and the bag should remain empty.
        assertFalse("remove() on an empty bag should return false", wasRemoved);
        assertTrue("The bag should remain empty after the remove attempt", collectionSortedBag.isEmpty());
    }
}