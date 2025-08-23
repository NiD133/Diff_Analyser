package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Contains tests for the {@link IndexedCollection} class, focusing on its behavior
 * when decorating unmodifiable collections.
 */
public class IndexedCollectionTest {

    /**
     * Tests that addAll() throws an UnsupportedOperationException if the underlying
     * decorated collection does not support the add operation.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addAllShouldThrowExceptionWhenDecoratedCollectionIsUnmodifiable() {
        // Arrange: Create an IndexedCollection decorating an unmodifiable collection.
        // The keySet() of a HashMap does not support add() or addAll().
        final Map<Integer, String> sourceMap = new HashMap<>();
        sourceMap.put(1, "one");
        final Set<Integer> unmodifiableKeySet = sourceMap.keySet();

        // The transformer is required for construction but its logic is not central to this test.
        final Transformer<Integer, Integer> identityTransformer = NOPTransformer.nopTransformer();
        final Collection<Integer> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(unmodifiableKeySet, identityTransformer);

        final Collection<Integer> collectionToAdd = Collections.singletonList(2);

        // Act: Attempt to add elements to the collection. This action is expected to fail
        // because it is delegated to the unmodifiable underlying keySet.
        indexedCollection.addAll(collectionToAdd);

        // Assert: The test will pass if an UnsupportedOperationException is thrown,
        // as declared in the @Test annotation.
    }
}