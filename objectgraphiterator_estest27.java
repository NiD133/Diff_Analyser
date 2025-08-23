package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link ObjectGraphIterator}.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that if the provided transformer throws an exception during iteration,
     * the call to {@link ObjectGraphIterator#next()} correctly propagates that exception.
     */
    @Test
    public void nextShouldPropagateExceptionFromTransformer() {
        // Arrange: Create an iterator with a transformer that is designed to always throw an exception.
        final Object root = new Object();
        final Transformer<Object, ?> exceptionThrowingTransformer = ExceptionTransformer.exceptionTransformer();
        final ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(root, exceptionThrowingTransformer);

        // Act & Assert: Verify that calling next() results in the expected RuntimeException.
        try {
            iterator.next();
            fail("A RuntimeException was expected but not thrown.");
        } catch (final RuntimeException e) {
            // The iterator should propagate the exception from the transformer without modification.
            final String expectedMessage = "ExceptionTransformer invoked";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}