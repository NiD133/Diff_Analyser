package org.apache.commons.collections4.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

/**
 * Contains tests for the static factory methods of {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    /**
     * Tests that {@link IndexedCollection#uniqueIndexedCollection(Collection, Transformer)}
     * throws an IllegalArgumentException if the provided collection contains elements
     * that result in duplicate keys.
     */
    @Test
    public void uniqueIndexedCollectionShouldThrowExceptionForDuplicateKeys() {
        // Arrange: Create a transformer that maps any object to the same constant key.
        final Transformer<String, String> keyTransformer = ConstantTransformer.constantTransformer("DUPLICATE_KEY");

        // Arrange: Create a source collection with two distinct elements. The keyTransformer
        // will generate the same key for both, violating the uniqueness constraint.
        final Collection<String> sourceCollection = new ArrayList<>();
        sourceCollection.add("element1");
        sourceCollection.add("element2");

        // Act & Assert: Attempt to create the unique indexed collection and verify that it
        // throws an IllegalArgumentException with the expected message.
        try {
            IndexedCollection.uniqueIndexedCollection(sourceCollection, keyTransformer);
            fail("Expected an IllegalArgumentException because of duplicate keys, but none was thrown.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals("Duplicate key in uniquely indexed collection.", e.getMessage());
        }
    }
}