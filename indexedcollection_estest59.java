package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link IndexedCollection} focusing on uniqueness constraints.
 */
public class IndexedCollectionUniquenessTest {

    /**
     * Tests that addAll() throws an IllegalArgumentException when an element
     * to be added maps to a key that already exists in a unique index.
     */
    @Test
    public void testAddAllWithDuplicateKeyToUniqueIndexThrowsException() {
        // Arrange
        // A transformer that maps every object to the same key (null).
        final Transformer<Object, Object> nullKeyTransformer = ConstantTransformer.nullTransformer();
        
        // The base collection that will be indexed. It will also be used as an element.
        final Collection<Object> baseCollection = new LinkedList<>();
        final IndexedCollection<Object, Object> uniqueCollection = 
                IndexedCollection.uniqueIndexedCollection(baseCollection, nullKeyTransformer);

        // Add the baseCollection to itself as an element. The key for this element is null.
        uniqueCollection.add(baseCollection);

        // Act & Assert
        // Now, try to add all elements from the baseCollection again. The baseCollection
        // contains itself as its only element. This attempts to add the same element,
        // which maps to the same key, causing a duplicate key violation.
        try {
            uniqueCollection.addAll(baseCollection);
            fail("Expected an IllegalArgumentException because of a duplicate key.");
        } catch (final IllegalArgumentException e) {
            // Assert that the correct exception was thrown with the expected message.
            assertEquals("Duplicate key in uniquely indexed collection.", e.getMessage());
        }
    }
}