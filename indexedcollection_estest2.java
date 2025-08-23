package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link IndexedCollection} class.
 */
public class IndexedCollectionTest {

    /**
     * Tests that an item added to a unique IndexedCollection can be successfully
     * retrieved using the values(key) method.
     */
    @Test
    public void values_shouldReturnCollectionContainingTheAddedItem_forUniqueIndex() {
        // Arrange
        // The transformer will always return a constant key (1) for any element added.
        final Integer key = 1;
        final Transformer<String, Integer> keyTransformer = ConstantTransformer.constantTransformer(key);

        // Create a unique indexed collection, which will enforce that each key maps to only one value.
        final Collection<String> sourceCollection = new LinkedList<>();
        final IndexedCollection<Integer, String> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(sourceCollection, keyTransformer);

        final String valueToAdd = "test-value";

        // Act
        // Add the element to the collection. It should be indexed under the key '1'.
        indexedCollection.add(valueToAdd);

        // Retrieve all values associated with the key.
        final Collection<String> retrievedValues = indexedCollection.values(key);

        // Assert
        // Verify that the retrieved collection is not null and contains exactly the added element.
        assertNotNull("The collection returned by values() should not be null.", retrievedValues);
        assertEquals("There should be exactly one value for the given key.", 1, retrievedValues.size());
        assertTrue("The retrieved collection should contain the added value.", retrievedValues.contains(valueToAdd));
    }
}