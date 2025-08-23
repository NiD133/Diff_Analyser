package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;

/**
 * Contains tests for the {@link IndexedCollection} class, focusing on modification scenarios.
 */
public class IndexedCollectionTest {

    /**
     * Tests that a ConcurrentModificationException is thrown when calling addAll()
     * with the collection's own underlying collection as the source.
     * <p>
     * This scenario causes a failure because the addAll() method iterates over the
     * underlying collection while the add() method, called internally, modifies it.
     * This is a classic example of concurrent modification.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void addAll_whenSourceIsTheUnderlyingCollection_shouldThrowCME() {
        // Arrange
        // The collection that will be decorated and also used as the source for addAll()
        Collection<String> underlyingCollection = new LinkedList<>();

        // A transformer to generate keys (the specific key doesn't matter for this test)
        Transformer<String, Object> keyTransformer = ConstantTransformer.nullTransformer();

        // The IndexedCollection under test, which decorates the underlying collection
        IndexedCollection<Object, String> indexedCollection =
            IndexedCollection.nonUniqueIndexedCollection(underlyingCollection, keyTransformer);

        // Add an initial element to ensure the collection is not empty before addAll is called.
        // This is necessary to trigger the iteration that leads to the exception.
        indexedCollection.add("initial element");

        // Act
        // Attempt to add all elements from the underlying collection back into itself
        // via the decorator. This will cause an iteration over underlyingCollection
        // while simultaneously modifying it.
        indexedCollection.addAll(underlyingCollection);

        // Assert
        // The test will pass if a ConcurrentModificationException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}