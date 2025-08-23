package org.apache.commons.collections4.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Contains test cases for the {@link IndexedCollection} class.
 */
public class IndexedCollectionTest {

    /**
     * Tests that adding an element to an IndexedCollection throws a NullPointerException
     * if the collection was initialized with a null key transformer.
     */
    @Test(expected = NullPointerException.class)
    public void addShouldThrowNullPointerExceptionWhenKeyTransformerIsNull() {
        // Arrange: Create an IndexedCollection with a null transformer.
        // The transformer is necessary to generate a key for any element being added.
        final Collection<Object> sourceCollection = new ArrayList<>();
        final IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(sourceCollection, null);

        // Act: Attempt to add an element to the collection.
        // This action should trigger the use of the null transformer.
        indexedCollection.add("any element");

        // Assert: A NullPointerException is expected, which is handled by the
        // @Test(expected=...) annotation.
    }
}