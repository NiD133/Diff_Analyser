package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for {@link ObjectGraphIterator}.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that if the provided Transformer throws an exception during the
     * first call to hasNext(), the exception is propagated to the caller.
     */
    @Test
    public void hasNextShouldPropagateExceptionFromFailingTransformer() {
        // Arrange
        final Integer root = 42;
        final String nonExistentMethodName = "thisMethodDoesNotExist";

        // This transformer is designed to fail by trying to invoke a method
        // that does not exist on the Integer class.
        final Transformer<Integer, ?> failingTransformer =
                new InvokerTransformer<>(nonExistentMethodName, null, null);

        final ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>(root, failingTransformer);

        // Act & Assert
        try {
            iterator.hasNext();
            fail("A RuntimeException was expected but not thrown.");
        } catch (final RuntimeException e) {
            // Verify that the exception is the one thrown by InvokerTransformer
            final String expectedMessage = "InvokerTransformer: The method '" + nonExistentMethodName +
                                           "' on 'class java.lang.Integer' does not exist";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}