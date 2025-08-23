package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Tests for {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    /**
     * Tests that calling reindex() on a collection with a null keyTransformer
     * throws a NullPointerException if the underlying collection is not empty.
     */
    @Test(expected = NullPointerException.class)
    public void reindexShouldThrowNullPointerExceptionWhenKeyTransformerIsNull() {
        // Arrange
        // Create a collection that will be decorated.
        Collection<Integer> sourceCollection = new LinkedList<>();

        // Create the IndexedCollection with a null keyTransformer.
        // The initial reindex() in the constructor does nothing as the collection is empty.
        IndexedCollection<Integer, Integer> indexedCollection =
            IndexedCollection.uniqueIndexedCollection(sourceCollection, null);

        // Directly modify the underlying collection, making the index out of sync.
        // This is the scenario reindex() is designed to fix.
        sourceCollection.add(42);

        // Act
        // Attempt to reindex. This should fail because the transformer is null
        // and it will be invoked on the element that was added to the source collection.
        indexedCollection.reindex();
    }
}