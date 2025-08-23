package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Contains tests for the {@link CollectionBag#retainAll(java.util.Collection)} method.
 */
public class CollectionBagTest {

    /**
     * Tests that calling {@code retainAll} with the decorated bag itself as an argument
     * results in no changes to the bag. The method should correctly identify that
     * all elements should be retained and return {@code false}.
     */
    @Test
    public void retainAllWithSelfAsArgumentShouldNotChangeTheBag() {
        // Arrange
        final Bag<String> decoratedBag = new TreeBag<>();
        final CollectionBag<String> collectionBag = new CollectionBag<>(decoratedBag);
        collectionBag.add("A", 3);
        collectionBag.add("B", 2);

        // Create a copy for verification before the operation.
        final Bag<String> expectedBag = new TreeBag<>(collectionBag);

        // Act
        final boolean wasModified = collectionBag.retainAll(decoratedBag);

        // Assert
        assertFalse("retainAll should return false as no elements were removed.", wasModified);
        assertEquals("The bag's contents should be unchanged.", expectedBag, collectionBag);
    }
}