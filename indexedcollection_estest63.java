package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertNull;

/**
 * This test class contains an improved version of a test from IndexedCollection_ESTestTest63.
 */
public class IndexedCollection_ESTestTest63 extends IndexedCollection_ESTest_scaffolding {

    /**
     * Tests that the values() method returns null when called with a key that does not exist in the index.
     * The original test was functionally correct but used confusing inputs, making its purpose unclear.
     */
    @Test
    public void valuesShouldReturnNullForNonExistentKey() {
        // Arrange
        // A transformer that uses an object's string representation as the key.
        final Transformer<Object, String> keyTransformer = Object::toString;

        // An empty source collection, ensuring no keys are present in the index.
        final Collection<Object> sourceCollection = new ArrayList<>();

        final IndexedCollection<String, Object> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(sourceCollection, keyTransformer);

        final String nonExistentKey = "a key that is guaranteed not to be in the index";

        // Act
        // Attempt to retrieve values for the non-existent key.
        final Collection<Object> result = indexedCollection.values(nonExistentKey);

        // Assert
        // The Javadoc for values(K) states it should return null if the key is not found.
        assertNull("Expected null when retrieving values for a non-existent key", result);
    }
}