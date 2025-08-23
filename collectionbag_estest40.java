package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains understandable tests for the {@link CollectionBag} class, focusing on
 * its adherence to the Collection contract and specific implementation details.
 */
public class CollectionBagTest {

    /**
     * Tests that calling removeAll() with a null collection argument
     * does not throw an exception and correctly returns false, indicating
     * that the bag was not modified.
     */
    @Test
    public void testRemoveAllWithNullCollectionReturnsFalse() {
        // Arrange: Create an empty CollectionBag. The behavior under test
        // is independent of the bag's contents.
        final Bag<String> bag = new CollectionBag<>(new HashBag<>());

        // Act: Call the removeAll method with a null argument.
        final boolean wasModified = bag.removeAll(null);

        // Assert: The method should return false, as the collection was not changed.
        assertFalse("removeAll(null) should return false, indicating no modification.", wasModified);
    }
}