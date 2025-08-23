package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link IndexedCollection} class.
 * This class focuses on improving a single, auto-generated test case for clarity.
 */
public class IndexedCollectionTest {

    /**
     * Tests that calling addAll() with an empty collection does not modify the
     * IndexedCollection and returns false, as per the Collection.addAll() contract.
     */
    @Test
    public void addAllWithEmptyCollectionShouldReturnFalseAndNotModifyCollection() {
        // Arrange
        // Create an empty collection decorated with an index.
        final Collection<Integer> baseCollection = new LinkedList<>();

        // A simple transformer that assigns a constant key to all elements.
        // The specific transformer logic is not important for this test.
        final Transformer<Integer, String> keyTransformer = ConstantTransformer.constantTransformer("KEY");

        final IndexedCollection<String, Integer> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(baseCollection, keyTransformer);

        // Act
        // Attempt to add an empty collection.
        final boolean wasModified = indexedCollection.addAll(Collections.emptyList());

        // Assert
        // The addAll method should return false because the collection was not changed.
        assertFalse("addAll() should return false when the input collection is empty.", wasModified);
        assertTrue("The collection should remain empty after adding an empty collection.", indexedCollection.isEmpty());
    }
}