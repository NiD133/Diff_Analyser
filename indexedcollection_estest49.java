package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    /**
     * Tests that adding an element which maps to an already existing key
     * in a unique IndexedCollection throws an IllegalArgumentException.
     */
    @Test
    public void addShouldThrowExceptionOnDuplicateKeyInUniqueCollection() {
        // Arrange
        // Use a transformer that generates a key from an object's property (e.g., String length).
        final Transformer<String, Integer> keyTransformer = String::length;
        final Collection<String> sourceCollection = new LinkedList<>();
        sourceCollection.add("Apple"); // This element will have a key of 5.

        final IndexedCollection<Integer, String> uniqueIndexedCollection =
                IndexedCollection.uniqueIndexedCollection(sourceCollection, keyTransformer);

        // This new element also has a length of 5, which creates a key collision.
        final String elementWithDuplicateKey = "Grape";

        // Act & Assert
        try {
            uniqueIndexedCollection.add(elementWithDuplicateKey);
            fail("Expected an IllegalArgumentException to be thrown for a duplicate key.");
        } catch (final IllegalArgumentException e) {
            // Verify the exception and its message are correct.
            assertEquals("Duplicate key in uniquely indexed collection.", e.getMessage());
        }
    }
}