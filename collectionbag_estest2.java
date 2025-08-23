package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.assertFalse;

/**
 * Test class for {@link CollectionBag}.
 */
public class CollectionBag_ESTestTest2 {

    /**
     * Tests that containsAll() returns false when the CollectionBag is empty
     * but the collection being checked is not. This aligns with the standard
     * java.util.Collection contract.
     */
    @Test
    public void containsAllShouldReturnFalseIfBagIsEmptyAndGivenCollectionIsNotEmpty() {
        // Arrange
        // CollectionBag is a decorator that makes a Bag behave like a standard Collection.
        // Here, we create an empty CollectionBag by decorating an empty HashBag.
        final Bag<Object> emptyBag = new HashBag<>();
        final Bag<Object> emptyCollectionBag = new CollectionBag<>(emptyBag);

        // The collection to check against contains one element.
        final Bag<Integer> collectionToCompare = new TreeBag<>();
        collectionToCompare.add(123);

        // Act
        // Check if the empty bag contains all elements from the non-empty collection.
        final boolean result = emptyCollectionBag.containsAll(collectionToCompare);

        // Assert
        // The result must be false, as an empty collection cannot contain any elements.
        assertFalse("An empty CollectionBag should not 'containAll' from a non-empty collection.", result);
    }
}