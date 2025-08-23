package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    @Test
    public void containsAllShouldReturnTrueForCollectionContainingNull() {
        // Arrange
        // Create a source collection containing a single null element.
        final Collection<Integer> sourceCollection = new LinkedList<>();
        sourceCollection.add(null);

        // Use a transformer that maps any element (including null) to a null key.
        final Transformer<Integer, Integer> nullKeyTransformer = ConstantTransformer.nullTransformer();

        // Create a unique indexed collection from the source.
        // The null element will be indexed with a null key.
        final IndexedCollection<Integer, Integer> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(sourceCollection, nullKeyTransformer);

        // Act
        // Check if the indexed collection contains all elements from the original source collection.
        final boolean containsAll = indexedCollection.containsAll(sourceCollection);

        // Assert
        // The result should be true, as the collection was initialized with these elements.
        assertTrue("containsAll should return true for the collection it was created from", containsAll);
    }
}