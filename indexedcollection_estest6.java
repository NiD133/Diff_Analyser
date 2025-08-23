package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link IndexedCollection}.
 * This test focuses on the contains() method.
 */
public class IndexedCollectionTest {

    /**
     * Tests that an element present in the original collection is correctly
     * found using the contains() method after the IndexedCollection is created.
     */
    @Test
    public void containsShouldReturnTrueForExistingElement() {
        // Arrange
        // 1. Create a source collection and add an element to it.
        Collection<Integer> sourceCollection = new LinkedList<>();
        Integer elementToFind = 3;
        sourceCollection.add(elementToFind);

        // 2. Define a simple transformer. All elements will be mapped to the same key.
        final String key = "CONSTANT_KEY";
        Transformer<Integer, String> keyTransformer = new ConstantTransformer<>(key);

        // 3. Create the IndexedCollection, which will index the elements from the source.
        IndexedCollection<String, Integer> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(sourceCollection, keyTransformer);

        // Act
        // Check if the collection contains the element that was added.
        boolean found = indexedCollection.contains(elementToFind);

        // Assert
        // The element should be found in the indexed collection.
        assertTrue("IndexedCollection should contain the element from the initial source collection", found);
    }
}