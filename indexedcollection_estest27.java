package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link IndexedCollection} class.
 */
public class IndexedCollectionTest {

    /**
     * Tests that {@link IndexedCollection#reindex()} throws an IllegalArgumentException
     * when used with a unique index and the underlying collection contains elements
     * that map to the same key.
     *
     * This scenario simulates an external modification to the decorated collection,
     * which puts the index out of sync.
     */
    @Test
    public void testReindexOnUniqueCollectionWithDuplicateKeysThrowsException() {
        // Arrange: Set up a collection and a transformer that will cause a key collision.

        // 1. The underlying collection that will be decorated.
        Collection<Object> underlyingCollection = new LinkedList<>();

        // 2. A transformer that maps any object to the same key, guaranteeing a collision.
        final String duplicateKey = "DUPLICATE_KEY";
        Transformer<Object, String> collapsingTransformer = new ConstantTransformer<>(duplicateKey);

        // 3. Create the unique indexed collection. At this point, it's empty and valid.
        // The constructor performs an initial, successful reindex on the empty collection.
        IndexedCollection<String, Object> uniqueCollection =
                IndexedCollection.uniqueIndexedCollection(underlyingCollection, collapsingTransformer);

        // 4. Modify the underlying collection directly, bypassing the IndexedCollection's add method.
        // This puts the index out of sync, which is the scenario reindex() is designed to fix.
        underlyingCollection.add("element1");
        underlyingCollection.add("element2");

        // At this point, the index is empty, but the decorated collection contains two elements
        // that will both map to the key "DUPLICATE_KEY".

        // Act & Assert: Attempt to reindex and verify that the expected exception is thrown.
        try {
            uniqueCollection.reindex();
            fail("reindex() should have thrown an IllegalArgumentException due to duplicate keys.");
        } catch (final IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("Duplicate key in uniquely indexed collection.", e.getMessage());
        }
    }
}