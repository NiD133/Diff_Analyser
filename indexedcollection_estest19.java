package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Contains tests for the {@link IndexedCollection} class, focusing on exception handling.
 */
public class IndexedCollectionExceptionTest {

    /**
     * Tests that removeAll() propagates the RuntimeException thrown by the key transformer.
     * The method must use the transformer to find the key for an element to be removed,
     * and any exception from the transformer should bubble up.
     */
    @Test(expected = RuntimeException.class)
    public void testRemoveAllPropagatesExceptionFromKeyTransformer() {
        // Arrange
        // 1. Create a transformer that is guaranteed to throw an exception.
        final Transformer<Object, Object> exceptionThrowingTransformer =
                ExceptionTransformer.exceptionTransformer();

        // 2. Create an IndexedCollection with the problematic transformer.
        //    The decorated collection is initially empty, so the constructor succeeds without
        //    invoking the transformer.
        final Collection<Object> decoratedCollection = new LinkedList<>();
        final IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(decoratedCollection, exceptionThrowingTransformer);

        // 3. Define a collection of items to attempt to remove.
        final Collection<String> itemsToRemove = Collections.singletonList("item to remove");

        // Act
        // Attempt to remove the items. This will invoke the key transformer for each item,
        // which will trigger the expected RuntimeException.
        indexedCollection.removeAll(itemsToRemove);

        // Assert (handled by the @Test(expected) annotation)
        // The test will pass only if a RuntimeException is thrown.
    }
}