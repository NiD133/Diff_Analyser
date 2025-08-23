package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for exceptional behavior in {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    /**
     * Tests that an exception thrown by the key transformer during a 'contains' check
     * is propagated to the caller.
     */
    @Test
    public void containsShouldPropagateExceptionFromKeyTransformer() {
        // Arrange: Create an IndexedCollection with a transformer that always throws an exception.
        final Collection<Integer> sourceCollection = new LinkedList<>();
        final Transformer<Integer, Integer> exceptionThrowingTransformer = ExceptionTransformer.exceptionTransformer();
        final IndexedCollection<Integer, Integer> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(sourceCollection, exceptionThrowingTransformer);

        // Act & Assert: Verify that calling contains() triggers the exception.
        try {
            // The 'contains' method will invoke the transformer on the given element.
            indexedCollection.contains(123);
            fail("Expected FunctorException to be thrown");
        } catch (final FunctorException e) {
            // The ExceptionTransformer is designed to throw a FunctorException with this specific message.
            assertEquals("ExceptionTransformer invoked", e.getMessage());
        }
    }
}