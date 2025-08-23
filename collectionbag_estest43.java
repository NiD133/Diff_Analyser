package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link CollectionBag}.
 */
public class CollectionBagTest {

    /**
     * Tests that calling addAll() with an empty collection does not modify the bag
     * and returns false, as specified by the java.util.Collection contract.
     */
    @Test
    public void addAllWithEmptyCollectionShouldReturnFalseAndNotModifyBag() {
        // Arrange: Create an empty CollectionBag.
        final Bag<Integer> decoratedBag = new TreeBag<>();
        final CollectionBag<Integer> bag = new CollectionBag<>(decoratedBag);

        // Act: Attempt to add all elements from an empty collection.
        final boolean wasModified = bag.addAll(Collections.emptyList());

        // Assert: The bag should not have been modified, and the method should return false.
        assertFalse("addAll() with an empty collection should return false.", wasModified);
        assertTrue("The bag should remain empty.", bag.isEmpty());
    }
}