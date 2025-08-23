package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    /**
     * Tests that addAll() successfully adds multiple elements to a non-unique index
     * and correctly updates both the collection and the index.
     */
    @Test
    public void addAllShouldAddElementsToNonUniqueIndex() {
        // Arrange
        // Use a simple String as the key for better readability.
        final String key = "GROUP_A";
        // This transformer will assign the same key to every element added.
        final Transformer<Integer, String> keyTransformer = ConstantTransformer.constantTransformer(key);

        // The collection that will be decorated and indexed. Start with an empty list.
        final Collection<Integer> decorated = new ArrayList<>();
        final IndexedCollection<String, Integer> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(decorated, keyTransformer);

        final List<Integer> elementsToAdd = Arrays.asList(100, 200);

        // Act
        final boolean wasModified = indexedCollection.addAll(elementsToAdd);

        // Assert
        // 1. Verify addAll returned true, indicating the collection was changed.
        assertTrue("addAll should return true when new elements are added.", wasModified);

        // 2. Verify the size and contents of the underlying collection.
        assertEquals("Collection size should be 2 after adding two elements.", 2, indexedCollection.size());
        assertTrue("Collection should contain the first added element.", indexedCollection.contains(100));
        assertTrue("Collection should contain the second added element.", indexedCollection.contains(200));

        // 3. Verify that the index was correctly updated.
        final Collection<Integer> valuesForKey = indexedCollection.values(key);
        assertEquals("The index should map the key to two values.", 2, valuesForKey.size());
        assertTrue("The index values for the key should contain the first element.", valuesForKey.contains(100));
        assertTrue("The index values for the key should contain the second element.", valuesForKey.contains(200));
    }
}