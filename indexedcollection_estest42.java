package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Contains tests for the {@link IndexedCollection} class.
 */
public class IndexedCollection_ESTestTest42 {

    /**
     * Tests that if the key transformer throws an exception during an add operation,
     * that exception is propagated to the caller.
     */
    @Test(expected = RuntimeException.class)
    public void addShouldPropagateExceptionFromKeyTransformer() {
        // Arrange
        final Collection<Object> sourceCollection = new LinkedList<>();
        
        // Use a transformer that is designed to always throw an exception upon invocation.
        final Transformer<Object, ?> exceptionThrowingTransformer = ExceptionTransformer.exceptionTransformer();
        
        final IndexedCollection<?, Object> indexedCollection =
            IndexedCollection.nonUniqueIndexedCollection(sourceCollection, exceptionThrowingTransformer);

        // Act & Assert
        // The add operation will invoke the transformer to generate a key.
        // Since the transformer throws a RuntimeException, the add method should fail with that same exception.
        // The @Test(expected) annotation asserts that the expected exception is thrown.
        indexedCollection.add("any object");
    }
}