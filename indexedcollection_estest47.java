package org.apache.commons.collections4.collection;

import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Contains tests for the {@link IndexedCollection} class.
 */
public class IndexedCollectionTest {

    /**
     * Tests that calling remove() on an IndexedCollection configured with a null
     * key transformer throws a NullPointerException.
     * <p>
     * The remove() method needs the transformer to calculate the key of the element
     * being removed so it can update its internal index. A null transformer will
     * inevitably cause a NullPointerException when invoked.
     */
    @Test(expected = NullPointerException.class)
    public void removeShouldThrowNullPointerExceptionWhenTransformerIsNull() {
        // Arrange: Create a collection and decorate it with an IndexedCollection
        // using a null key transformer.
        Collection<Integer> sourceCollection = new LinkedList<>();
        IndexedCollection<Integer, Integer> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(sourceCollection, null);

        Integer elementToRemove = 35;
        sourceCollection.add(elementToRemove); // Add an element to ensure remove() is fully executed.

        // Act & Assert: Attempting to remove the element must call the null transformer,
        // which is expected to throw a NullPointerException.
        indexedCollection.remove(elementToRemove);
    }
}