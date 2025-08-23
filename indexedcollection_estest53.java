package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link IndexedCollection} class.
 */
public class IndexedCollectionTest {

    /**
     * Tests that removeIf correctly removes an element that matches the predicate
     * and returns true, confirming the collection was modified.
     */
    @Test
    public void removeIfShouldRemoveMatchingElementAndReturnTrue() {
        // Arrange
        // Create a collection to be decorated.
        final Collection<Object> underlyingCollection = new LinkedList<>();

        // Use a transformer that maps any element to the same key (null).
        // This is a simple way to set up the index for the test.
        final Transformer<Object, Object> keyTransformer = ConstantTransformer.nullTransformer();

        final IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(underlyingCollection, keyTransformer);

        // Add an element to the collection. We use the collection itself as the element
        // to ensure we have a unique object to target for removal.
        final Object elementToRemove = underlyingCollection;
        indexedCollection.add(elementToRemove);

        // Create a predicate that will match only the element we just added.
        final Predicate<Object> removalPredicate = EqualPredicate.equalPredicate(elementToRemove);

        // Act
        // Attempt to remove the element using the predicate.
        final boolean wasRemoved = indexedCollection.removeIf(removalPredicate);

        // Assert
        // Verify that the method reported a modification.
        assertTrue("removeIf should return true when an element is removed.", wasRemoved);

        // Verify that the underlying collection is now empty.
        assertEquals("The underlying collection should be empty after the element is removed.", 0, underlyingCollection.size());
    }
}