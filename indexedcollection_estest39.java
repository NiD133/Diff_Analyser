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
 * Contains tests for the {@link IndexedCollection} class.
 */
public class IndexedCollectionTest {

    /**
     * Tests that if the key transformer throws an exception during an addAll operation,
     * the exception is propagated to the caller.
     */
    @Test
    public void addAllShouldPropagateExceptionFromKeyTransformer() {
        // Arrange
        // 1. Create a transformer that always throws a RuntimeException.
        final Transformer<Integer, Integer> exceptionThrowingTransformer =
                ExceptionTransformer.exceptionTransformer();

        // 2. Create an IndexedCollection using this transformer.
        //    The decorated collection is initially empty.
        final IndexedCollection<Integer, Integer> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(new LinkedList<>(), exceptionThrowingTransformer);

        // 3. Create a collection with an element to add.
        final Collection<Integer> elementsToAdd = Collections.singletonList(-574);

        // Act & Assert
        try {
            indexedCollection.addAll(elementsToAdd);
            fail("Expected a RuntimeException to be thrown by the ExceptionTransformer.");
        } catch (final RuntimeException e) {
            // Verify that the caught exception is the one thrown by the transformer.
            assertEquals("ExceptionTransformer invoked", e.getMessage());
        }
    }
}