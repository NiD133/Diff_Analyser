package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CollectionBag#retainAll(java.util.Collection)} method.
 */
public class CollectionBagRetainAllTest {

    /**
     * Tests that retainAll() returns false and does not modify the bag
     * when the collection to be retained contains all elements of the bag.
     */
    @Test
    public void retainAllShouldReturnFalseWhenNoElementsAreRemoved() {
        // Arrange
        final Bag<String> decoratedBag = new HashBag<>();
        final CollectionBag<String> collectionBag = new CollectionBag<>(decoratedBag);
        collectionBag.add("A");

        // The collection to retain contains the same element as the bag.
        final Bag<String> elementsToRetain = new HashBag<>();
        elementsToRetain.add("A");

        // Act
        // retainAll should not modify the bag, as all its elements are in 'elementsToRetain'.
        final boolean wasModified = collectionBag.retainAll(elementsToRetain);

        // Assert
        assertFalse("retainAll should return false as the bag was not modified", wasModified);
        assertEquals("The bag size should remain unchanged", 1, collectionBag.size());
        assertTrue("The bag should still contain the element 'A'", collectionBag.contains("A"));
    }

    /**
     * Tests that retainAll() returns true and correctly removes elements
     * that are not present in the given collection.
     */
    @Test
    public void retainAllShouldReturnTrueWhenElementsAreRemoved() {
        // Arrange
        final Bag<String> decoratedBag = new HashBag<>();
        final CollectionBag<String> collectionBag = new CollectionBag<>(decoratedBag);
        collectionBag.add("A");
        collectionBag.add("B");

        // The collection to retain contains only one of the bag's elements.
        final Bag<String> elementsToRetain = new HashBag<>();
        elementsToRetain.add("B");

        // Act
        // retainAll should remove "A" from the bag.
        final boolean wasModified = collectionBag.retainAll(elementsToRetain);

        // Assert
        assertTrue("retainAll should return true as the bag was modified", wasModified);
        assertEquals("The bag size should be reduced to 1", 1, collectionBag.size());
        assertFalse("The bag should no longer contain 'A'", collectionBag.contains("A"));
        assertTrue("The bag should still contain 'B'", collectionBag.contains("B"));
    }
}