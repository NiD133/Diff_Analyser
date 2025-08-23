package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Test case for the {@link CollectionBag#removeAll(java.util.Collection)} method.
 */
public class CollectionBag_ESTestTest39 {

    /**
     * Tests that calling removeAll on an empty CollectionBag with an empty collection
     * does not modify the bag and correctly returns false.
     */
    @Test
    public void testRemoveAllOnEmptyBagWithEmptyCollectionShouldReturnFalse() {
        // Arrange: Create an empty CollectionBag. The underlying bag implementation (HashBag)
        // is chosen for simplicity.
        final Bag<Object> bag = new CollectionBag<>(new HashBag<>());

        // Act: Attempt to remove all elements from an empty collection.
        final boolean wasModified = bag.removeAll(Collections.emptySet());

        // Assert: The bag should not have been modified, so the method must return false.
        assertFalse("removeAll should return false as the bag was not changed", wasModified);
        assertTrue("The bag should remain empty after the operation", bag.isEmpty());
    }
}