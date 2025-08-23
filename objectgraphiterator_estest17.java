package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test suite contains tests for the {@link ObjectGraphIterator} class.
 * This specific test focuses on how the iterator handles failures within its internal traversal logic.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that a RuntimeException thrown by the transformer is propagated
     * when the iterator tries to find the next element.
     *
     * This test specifically calls the protected `updateCurrentIterator()` method
     * to verify that it correctly handles a misbehaving Transformer.
     */
    @Test
    public void updateCurrentIteratorShouldPropagateExceptionFromTransformer() {
        // Arrange: Create an iterator with a transformer that always throws an exception.
        final Object root = new Object();
        final Transformer<Object, ?> exceptionTransformer = ExceptionTransformer.exceptionTransformer();
        final ObjectGraphIterator<Object> graphIterator = new ObjectGraphIterator<>(root, exceptionTransformer);

        // Act & Assert: Expect a RuntimeException when updating the iterator's state.
        try {
            graphIterator.updateCurrentIterator();
            fail("A RuntimeException should have been thrown by the transformer.");
        } catch (final RuntimeException e) {
            // Verify that the caught exception is the one from ExceptionTransformer.
            assertEquals("ExceptionTransformer invoked", e.getMessage());
        }
    }
}