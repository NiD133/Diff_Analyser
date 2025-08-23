package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the factory methods of {@link IndexedCollection}.
 * This class focuses on specific failure scenarios.
 */
public class IndexedCollection_ESTestTest10 extends IndexedCollection_ESTest_scaffolding {

    /**
     * Tests that creating an IndexedCollection fails if the provided key transformer
     * throws an exception during the initial indexing of the source collection.
     * The constructor is expected to propagate this exception.
     */
    @Test
    public void uniqueIndexedCollectionCreationShouldFailWhenTransformerThrows() {
        // Arrange: Set up the test conditions.
        // 1. A source collection with at least one element. The element's value is irrelevant.
        final Collection<Integer> sourceCollection = Collections.singletonList(42);

        // 2. A transformer designed to always throw a RuntimeException upon invocation.
        final Transformer<Integer, ?> exceptionThrowingTransformer =
                ExceptionTransformer.exceptionTransformer();

        // Act & Assert: Execute the code under test and verify the outcome.
        try {
            // The factory method attempts to create the IndexedCollection.
            // During its construction, it iterates through the sourceCollection and applies the
            // transformer to each element to build the index. This is where the exception occurs.
            IndexedCollection.uniqueIndexedCollection(sourceCollection, exceptionThrowingTransformer);
            fail("Expected a RuntimeException because the transformer should have been invoked and thrown.");
        } catch (final RuntimeException e) {
            // Verify that the exception caught is the one thrown by our transformer.
            // This confirms the exception was correctly propagated.
            assertEquals("ExceptionTransformer invoked", e.getMessage());
        }
    }
}