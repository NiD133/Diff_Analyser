package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Note: The original test class name and inheritance are preserved.
// Unused imports from the original test have been removed for clarity.
public class CollectionBag_ESTestTest42 extends CollectionBag_ESTest_scaffolding {

    /**
     * Tests that calling removeAll on an empty CollectionBag with a non-empty
     * collection does not change the bag and correctly returns false.
     */
    @Test
    public void removeAllFromEmptyBagShouldReturnFalseAndNotModifyBag() {
        // Arrange
        // Create the bag under test, which is empty.
        final Bag<String> emptyBag = new CollectionBag<>(new HashBag<>());

        // Create a non-empty collection of elements to attempt to remove.
        final Bag<String> collectionWithElements = new CollectionBag<>(new HashBag<>());
        collectionWithElements.add("element_to_remove");

        // Act
        // Attempt to remove the elements from the empty bag.
        final boolean wasModified = emptyBag.removeAll(collectionWithElements);

        // Assert
        // 1. The operation should report that the bag was not modified.
        assertFalse("removeAll on an empty bag should return false", wasModified);

        // 2. The bag under test should remain empty.
        assertTrue("The empty bag should still be empty after the removeAll operation", emptyBag.isEmpty());
    }
}