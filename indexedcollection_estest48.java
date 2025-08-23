package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link IndexedCollection} class, focusing on its indexing capabilities.
 */
public class IndexedCollectionTest {

    /**
     * Tests that reindex() correctly updates the index after the underlying
     * collection has been modified directly. The Javadoc for IndexedCollection
     * explicitly states this is the intended use case for reindex().
     */
    @Test
    public void reindexShouldUpdateIndexWhenUnderlyingCollectionIsModifiedDirectly() {
        // Arrange: Create an IndexedCollection and an object to be added.
        // The transformer is configured to map every element to a 'null' key.
        final Collection<Object> underlyingCollection = new LinkedList<>();
        final Transformer<Object, Integer> nullKeyTransformer = new ConstantTransformer<>(null);
        final IndexedCollection<Integer, Object> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(underlyingCollection, nullKeyTransformer);

        // The index is initially empty.
        assertNull("The index should be empty before any modifications", indexedCollection.get(null));

        // Act (Part 1): Modify the underlying collection directly.
        // This action makes the index out of sync with the collection's actual content.
        final Object newItem = "newItem";
        underlyingCollection.add(newItem);

        // Assert (Part 1): Verify the index is now out of sync.
        // The object exists in the collection but is not yet findable via the index.
        assertTrue("Underlying collection should contain the new item", underlyingCollection.contains(newItem));
        assertNull("Index should not contain the new item before reindexing", indexedCollection.get(null));

        // Act (Part 2): Call reindex() to synchronize the index with the collection.
        indexedCollection.reindex();

        // Assert (Part 2): Verify the index now correctly reflects the collection's state.
        final Collection<Object> valuesForNullKey = indexedCollection.values(null);
        assertEquals("Index should now contain one value for the null key", 1, valuesForNullKey.size());
        assertTrue("Index should contain the newly added item after reindex", valuesForNullKey.contains(newItem));
    }
}