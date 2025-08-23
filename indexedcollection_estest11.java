package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Contains tests for the factory methods of {@link IndexedCollection}.
 */
public class IndexedCollectionFactoryTest {

    /**
     * Tests that creating a unique indexed collection fails if the key transformer
     * produces duplicate keys for elements in the source collection.
     * An IllegalArgumentException is expected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void uniqueIndexedCollection_withDuplicateKeys_throwsIllegalArgumentException() {
        // Arrange: Create a source collection and a transformer that will generate duplicate keys.
        // In this case, both "Apple" and "Apricot" will be mapped to the same key: 'A'.
        final List<String> sourceCollection = Arrays.asList("Apple", "Banana", "Apricot");
        final Transformer<String, Character> firstLetterTransformer = input -> input.charAt(0);

        // Act & Assert:
        // Attempting to create a unique indexed collection should throw an IllegalArgumentException
        // because the key 'A' is generated for more than one element.
        IndexedCollection.uniqueIndexedCollection(sourceCollection, firstLetterTransformer);
    }
}