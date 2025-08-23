package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for exception handling in {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    /**
     * Tests that if the keyTransformer throws an exception, the exception
     * is propagated up when calling containsAll().
     */
    @Test
    public void containsAllShouldPropagateExceptionFromKeyTransformer() {
        // Arrange
        // 1. Create a transformer that is designed to always throw a RuntimeException.
        final Transformer<Integer, Integer> exceptionThrowingTransformer =
                ExceptionTransformer.exceptionTransformer();

        // 2. Create an IndexedCollection with the faulty transformer.
        // The underlying collection can be empty, as its contents are not relevant for this test.
        final Collection<Integer> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(new LinkedList<>(), exceptionThrowingTransformer);

        // 3. Create a collection with one element to pass to containsAll().
        final Collection<Integer> collectionToSearchFor = Collections.singletonList(1);

        // Act & Assert
        // The containsAll() method iterates its input collection and uses the keyTransformer
        // on each element to check for its presence. This invocation should trigger the exception.
        try {
            indexedCollection.containsAll(collectionToSearchFor);
            fail("Expected a RuntimeException to be thrown by the key transformer.");
        } catch (final RuntimeException e) {
            // Verify that the caught exception is the one thrown by ExceptionTransformer.
            assertEquals("ExceptionTransformer invoked", e.getMessage());
        }
    }
}